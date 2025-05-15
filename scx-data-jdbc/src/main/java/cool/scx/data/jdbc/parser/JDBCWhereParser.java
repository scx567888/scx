package cool.scx.data.jdbc.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.data.query.Where;
import cool.scx.data.query.WhereClause;
import cool.scx.data.query.exception.WrongWhereParamTypeException;
import cool.scx.data.query.exception.WrongWhereTypeParamSizeException;
import cool.scx.data.query.parser.WhereParser;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sql.SQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static cool.scx.common.util.ArrayUtils.toObjectArray;
import static cool.scx.common.util.ObjectUtils.toJson;
import static cool.scx.data.jdbc.parser.JDBCColumnNameParser.splitIntoColumnNameAndFieldPath;
import static cool.scx.data.query.QueryBuilder.and;
import static cool.scx.data.query.QueryBuilder.or;
import static java.util.Collections.addAll;

/// JDBCDaoWhereParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCWhereParser extends WhereParser {

    private final JDBCColumnNameParser columnNameParser;
    private final Dialect dialect;

    public JDBCWhereParser(JDBCColumnNameParser columnNameParser, Dialect dialect) {
        this.columnNameParser = columnNameParser;
        this.dialect = dialect;
    }

    public WhereClause parseIsNull(Where w) {

        var columnName = columnNameParser.parseColumnName(w);

        return switch (w.whereType()) {
            case EQ -> new WhereClause(columnName + " IS NULL");
            case NE -> new WhereClause(columnName + " IS NOT NULL");
            default -> throw new IllegalArgumentException("Unexpected value: " + w.whereType());
        };

    }

    @Override
    public WhereClause parseEqual(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause("");
            } else {
                return parseIsNull(w);
            }
        }

        // 类似这种 "name = " 
        var columnDefinition = columnNameParser.parseColumnName(w) + " " + getWhereKeyWord(w) + " ";

        //针对 参数类型是 SQL 的情况进行特殊处理 下同
        if (w.value1() instanceof SQL a) {
            return new WhereClause(columnDefinition + "(" + a.sql() + ")", a.params());
        }

        return new WhereClause(columnDefinition + "?", w.value1());

    }

    @Override
    public WhereClause parseLike(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause("");
            } else {
                throw new WrongWhereTypeParamSizeException(w.name(), w.whereType(), 1);
            }
        }

        // 类似这种 "name = " 
        var columnDefinition = columnNameParser.parseColumnName(w) + " " + getWhereKeyWord(w) + " ";

        if (w.value1() instanceof SQL a) {
            return new WhereClause(columnDefinition + "CONCAT('%',(" + a.sql() + "),'%')", a.params());
        }

        return new WhereClause(columnDefinition + "CONCAT('%',?,'%')", w.value1());

    }

    @Override
    public WhereClause parseIn(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause("");
            } else {
                throw new WrongWhereTypeParamSizeException(w.name(), w.whereType(), 1);
            }
        }

        var columnName = columnNameParser.parseColumnName(w);

        if (w.value1() instanceof SQL a) {
            return new WhereClause(columnName + " " + getWhereKeyWord(w) + " " + "(" + a.sql() + ")", a.params());
        }

        var v = new Object[]{};
        try {
            v = toObjectArray(w.value1());
        } catch (Exception e) {
            throw new WrongWhereParamTypeException(w.name(), w.whereType(), "数组");
        }

        //0, 先处理空数组
        if (v.length == 0) {
            if (w.info().skipIfEmptyList()) {
                return new WhereClause("");
            } else {
                return switch (w.whereType()) {
                    case IN -> new WhereClause(dialect.falseExpression());
                    case NOT_IN -> new WhereClause(dialect.trueExpression());
                    default -> throw new IllegalArgumentException("Unexpected value: " + w.whereType());
                };
            }
        }

        //1, 处理参数
        var nonNullValues = Arrays.stream(v).filter(Objects::nonNull).distinct().toArray();
        var containsNull = Arrays.stream(v).anyMatch(Objects::isNull);

        //需要包含 null
        if (containsNull) {
            var nullClause = switch (w.whereType()) {
                case IN -> new WhereClause(columnName + " IS NULL");
                case NOT_IN -> new WhereClause(columnName + " IS NOT NULL");
                default -> throw new IllegalArgumentException("Unexpected value: " + w.whereType());
            };

            //只需要返回 null 即可
            if (nonNullValues.length == 0) {
                return nullClause;
            }

            var placeholder = StringUtils.repeat("?", ", ", nonNullValues.length);

            var inClause = new WhereClause(columnName + " " + getWhereKeyWord(w) + " (" + placeholder + ")", nonNullValues);

            var j = switch (w.whereType()) {
                case IN -> or(inClause, nullClause);
                case NOT_IN -> and(inClause, nullClause);
                default -> throw new IllegalArgumentException("Unexpected value: " + w.whereType());
            };

            return parseJunction(j);
        }

        var placeholder = StringUtils.repeat("?", ", ", nonNullValues.length);

        return new WhereClause(columnName + " " + getWhereKeyWord(w) + " (" + placeholder + ")", nonNullValues);

    }

    @Override
    public WhereClause parseBetween(Where w) {
        if (w.value1() == null || w.value2() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause("");
            } else {
                throw new WrongWhereTypeParamSizeException(w.name(), w.whereType(), 2);
            }
        }

        var columnName = columnNameParser.parseColumnName(w);

        var columnDefinition = columnName + " " + getWhereKeyWord(w) + " ";

        String v1;
        String v2;
        var whereParams = new ArrayList<>();
        if (w.value1() instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            addAll(whereParams, a.params());
        } else {
            v1 = "?";
            whereParams.add(w.value1());
        }
        if (w.value2() instanceof SQL a) {
            v2 = "(" + a.sql() + ")";
            addAll(whereParams, a.params());
        } else {
            v2 = "?";
            whereParams.add(w.value2());
        }

        return new WhereClause(columnDefinition + v1 + " AND " + v2, whereParams.toArray());
    }

    @Override
    public WhereClause parseJsonContains(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause("");
            } else {
                throw new WrongWhereTypeParamSizeException(w.name(), w.whereType(), 1);
            }
        }
        var c = splitIntoColumnNameAndFieldPath(w.name());
        var columnName = columnNameParser.parseColumnName(c.columnName(), w.info().useOriginalName());
        if (StringUtils.isBlank(c.columnName())) {
            throw new IllegalArgumentException("使用 " + w.whereType() + " 时, 查询名称不合法 !!! 字段名 : " + w.name());
        }
        String v1;
        Object[] whereParams;
        if (w.value1() instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            v1 = "?";
            if (w.info().useOriginalValue()) {
                whereParams = new Object[]{w.value1()};
            } else {
                try {
                    whereParams = new Object[]{toJson(w.value1(), new ObjectUtils.Options().setIgnoreJsonIgnore(true).setIgnoreNullValue(true))};
                } catch (JsonProcessingException e) {
                    throw new IllegalArgumentException("使用 " + w.whereType() + " 时, 查询参数不合法(无法正确转换为 JSON) !!! 字段名 : " + w.name(), e);
                }
            }
        }
        var whereClause = getWhereKeyWord(w) + "(" + columnName;
        if (StringUtils.notBlank(c.fieldPath())) {
            whereClause = whereClause + ", " + v1 + ", '$" + c.fieldPath() + "')";
        } else {
            whereClause = whereClause + ", " + v1 + ")";
        }
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parse(Object obj) {
        if (obj instanceof SQL sql) {
            return parseSQL(sql);
        }
        return super.parse(obj);
    }

    private WhereClause parseSQL(SQL sql) {
        return new WhereClause("(" + sql.sql() + ")", sql.params());
    }

    public String getWhereKeyWord(Where where) {
        return switch (where.whereType()) {
            case EQ -> "=";
            case NE -> "<>";
            case LT -> "<";
            case LTE -> "<=";
            case GT -> ">";
            case GTE -> ">=";
            case LIKE, LIKE_REGEX -> "LIKE";
            case NOT_LIKE, NOT_LIKE_REGEX -> "NOT LIKE";
            case IN -> "IN";
            case NOT_IN -> "NOT IN";
            case BETWEEN -> "BETWEEN";
            case NOT_BETWEEN -> "NOT BETWEEN";
            case JSON_CONTAINS -> "JSON_CONTAINS";
            case JSON_OVERLAPS -> "JSON_OVERLAPS";
        };
    }

}
