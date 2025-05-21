package cool.scx.data.jdbc.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.data.query.*;
import cool.scx.data.jdbc.exception.WrongWhereParamTypeException;
import cool.scx.data.jdbc.exception.WrongWhereTypeParamSizeException;
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
public class JDBCWhereParser {

    private final JDBCColumnNameParser columnNameParser;
    private final Dialect dialect;

    public JDBCWhereParser(JDBCColumnNameParser columnNameParser, Dialect dialect) {
        this.columnNameParser = columnNameParser;
        this.dialect = dialect;
    }

    public WhereClause parse(Object obj) {
        return switch (obj) {
            case String s -> parseString(s);
            case WhereClause w -> parseWhereClause(w);
            case Junction j -> parseJunction(j);
            case Not n -> parseNot(n);
            case Object[] o -> parseAll(o);
            case SQL sql -> parseSQL(sql);
            case Where w -> parseWhere(w);
            case Query q -> parseQuery(q);
            case null -> new WhereClause(null);
            default -> throw new IllegalArgumentException("Unsupported object type: " + obj.getClass());
        };
    }

    private WhereClause parseString(String s) {
        // 我们无法确定用户输入的内容 为了安全起见 我们为这种自定义查询 两端拼接 ()
        // 保证在和其他子句拼接的时候不产生歧义
        return new WhereClause("(" + s + ")");
    }

    private WhereClause parseWhereClause(WhereClause w) {
        return w;
    }

    private WhereClause parseJunction(Junction j) {
        var clauses = new ArrayList<String>();
        var whereParams = new ArrayList<>();
        for (var c : j.clauses()) {
            var w = parse(c);
            if (w != null && !w.isEmpty()) {
                clauses.add(w.whereClause());
                addAll(whereParams, w.params());
            }
        }

        if (clauses.isEmpty()) {
            return new WhereClause(null);
        }

        var clause = String.join(" " + getJunctionKeyWord(j) + " ", clauses);
        //只有 子句数量 大于 1 时, 我们才在两端拼接 括号
        if (clauses.size() > 1) {
            clause = "(" + clause + ")";
        }
        return new WhereClause(clause, whereParams.toArray());
    }

    protected WhereClause parseNot(Not n) {

        var w = parse(n.clause());

        if (w != null && !w.isEmpty()) {
            //因为其余解析方法已经保证了在可能出现歧义的子句两端拼接了括号, 所以这里直接添加 NOT 即可
            return new WhereClause(getNotKeyWord(n) + " " + w.whereClause(), w.params());
        } else {
            return new WhereClause(null);
        }

    }

    @Override
    protected WhereClause parseEQ(Where w) {

        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause(null);
            } else {
                var columnName = columnNameParser.parseColumnName(w);

                return switch (w.whereType()) {
                    case EQ -> new WhereClause(columnName + " IS NULL");
                    case NE -> new WhereClause(columnName + " IS NOT NULL");
                    default -> throw new IllegalArgumentException("Unexpected value: " + w.whereType());
                };
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
    protected WhereClause parseNE(Where w) {
        return parseEQ(w);
    }

    @Override
    protected WhereClause parseLT(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause(null);
            } else {
                throw new WrongWhereTypeParamSizeException(w.selector(), w.whereType(), 1);
            }
        }

        //针对 参数类型是 SQL 的情况进行特殊处理 下同
        if (w.value1() instanceof SQL a) {
            return new WhereClause(columnNameParser.parseColumnName(w) + " " + getWhereKeyWord(w) + " (" + a.sql() + ")", a.params());
        }

        return new WhereClause(columnNameParser.parseColumnName(w) + " " + getWhereKeyWord(w) + " ?", w.value1());
    }

    @Override
    protected WhereClause parseLTE(Where where) {
        return parseLT(where);
    }

    @Override
    protected WhereClause parseGT(Where where) {
        return parseLT(where);
    }

    @Override
    protected WhereClause parseGTE(Where where) {
        return parseLT(where);
    }

    @Override
    protected WhereClause parseLIKE(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause(null);
            } else {
                throw new WrongWhereTypeParamSizeException(w.selector(), w.whereType(), 1);
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
    protected WhereClause parseNOT_LIKE(Where where) {
        return parseLIKE(where);
    }

    @Override
    protected WhereClause parseLIKE_REGEX(Where where) {
        return parseLT(where);
    }

    @Override
    protected WhereClause parseNOT_LIKE_REGEX(Where where) {
        return parseLT(where);
    }

    @Override
    public WhereClause parseIN(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause(null);
            } else {
                throw new WrongWhereTypeParamSizeException(w.selector(), w.whereType(), 1);
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
            throw new WrongWhereParamTypeException(w.selector(), w.whereType(), "数组");
        }

        //0, 先处理空数组
        if (v.length == 0) {
            if (w.info().skipIfEmptyList()) {
                return new WhereClause(null);
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
    protected WhereClause parseNOT_IN(Where where) {
        return parseIN(where);
    }

    @Override
    protected WhereClause parseBETWEEN(Where w) {
        if (w.value1() == null || w.value2() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause(null);
            } else {
                throw new WrongWhereTypeParamSizeException(w.selector(), w.whereType(), 2);
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
    protected WhereClause parseNOT_BETWEEN(Where where) {
        return parseBETWEEN(where);
    }

    @Override
    protected WhereClause parseJSON_CONTAINS(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause(null);
            } else {
                throw new WrongWhereTypeParamSizeException(w.selector(), w.whereType(), 1);
            }
        }
        var c = splitIntoColumnNameAndFieldPath(w.selector());
        var columnName = columnNameParser.parseColumnName(c.columnName(), w.info().useExpression());
        if (StringUtils.isBlank(c.columnName())) {
            throw new IllegalArgumentException("使用 " + w.whereType() + " 时, 查询名称不合法 !!! 字段名 : " + w.selector());
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
                    throw new IllegalArgumentException("使用 " + w.whereType() + " 时, 查询参数不合法(无法正确转换为 JSON) !!! 字段名 : " + w.selector(), e);
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
    protected WhereClause parseJSON_OVERLAPS(Where where) {
        return parseJSON_CONTAINS(where);
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

  


 

    protected String getJunctionKeyWord(Junction junction) {
        return switch (junction) {
            case Or _ -> "OR";
            case And _ -> "AND";
        };
    }

  

    protected String getNotKeyWord(Not n) {
        return "NOT";
    }

   

    protected final WhereClause parseAll(Object[] objs) {
        var clauses = new ArrayList<String>();
        var whereParams = new ArrayList<>();
        for (var obj : objs) {
            var w = parse(obj);
            if (w != null && !w.isEmpty()) {
                clauses.add(w.whereClause());
                addAll(whereParams, w.params());
            }
        }

        if (clauses.isEmpty()) {
            return new WhereClause(null);
        }

        return new WhereClause(String.join("", clauses), whereParams.toArray());
    }

    protected WhereClause parseWhere(Where body) {
        return switch (body.whereType()) {
            case EQ -> parseEQ(body);
            case NE -> parseNE(body);
            case LT -> parseLT(body);
            case LTE -> parseLTE(body);
            case GT -> parseGT(body);
            case GTE -> parseGTE(body);
            case LIKE -> parseLIKE(body);
            case NOT_LIKE -> parseNOT_LIKE(body);
            case LIKE_REGEX -> parseLIKE_REGEX(body);
            case NOT_LIKE_REGEX -> parseNOT_LIKE_REGEX(body);
            case IN -> parseIN(body);
            case NOT_IN -> parseNOT_IN(body);
            case BETWEEN -> parseBETWEEN(body);
            case NOT_BETWEEN -> parseNOT_BETWEEN(body);
            case JSON_CONTAINS -> parseJSON_CONTAINS(body);
            case JSON_OVERLAPS -> parseJSON_OVERLAPS(body);
        };
    }

    protected abstract WhereClause parseEQ(Where where);

    protected abstract WhereClause parseNE(Where where);

    protected abstract WhereClause parseLT(Where where);

    protected abstract WhereClause parseLTE(Where where);

    protected abstract WhereClause parseGT(Where where);

    protected abstract WhereClause parseGTE(Where where);

    protected abstract WhereClause parseLIKE(Where where);

    protected abstract WhereClause parseNOT_LIKE(Where where);

    protected abstract WhereClause parseLIKE_REGEX(Where where);

    protected abstract WhereClause parseNOT_LIKE_REGEX(Where where);

    protected abstract WhereClause parseIN(Where where);

    protected abstract WhereClause parseNOT_IN(Where where);

    protected abstract WhereClause parseBETWEEN(Where where);

    protected abstract WhereClause parseNOT_BETWEEN(Where where);

    protected abstract WhereClause parseJSON_CONTAINS(Where where);

    protected abstract WhereClause parseJSON_OVERLAPS(Where where);

    private WhereClause parseQuery(Query query) {
        return parse(query.getWhere());
    }

}
