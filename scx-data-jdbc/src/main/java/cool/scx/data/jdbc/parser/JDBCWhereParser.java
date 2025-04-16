package cool.scx.data.jdbc.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.data.query.Where;
import cool.scx.data.query.WhereClause;
import cool.scx.data.query.exception.ValidParamListIsEmptyException;
import cool.scx.data.query.exception.WrongWhereParamTypeException;
import cool.scx.data.query.exception.WrongWhereTypeParamSizeException;
import cool.scx.data.query.parser.WhereParser;
import cool.scx.jdbc.sql.SQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static cool.scx.common.util.ArrayUtils.toObjectArray;
import static cool.scx.common.util.ObjectUtils.toJson;
import static cool.scx.data.jdbc.parser.JDBCColumnNameParser.splitIntoColumnNameAndFieldPath;
import static java.util.Collections.addAll;

/// JDBCDaoWhereParser
///
/// @author scx567888
/// @version 0.0.1
public class JDBCWhereParser extends WhereParser {

    private final JDBCColumnNameParser columnNameParser;

    public JDBCWhereParser(JDBCColumnNameParser columnNameParser) {
        this.columnNameParser = columnNameParser;
    }

    @Override
    public WhereClause parseIsNull(Where w) {
        var columnName = columnNameParser.parseColumnName(w);
        var whereParams = new Object[]{};
        var whereClause = columnName + " " + getWhereKeyWord(w.whereType());
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseEqual(Where w) {
        if (w.value1() == null) {
            if (w.info().skipIfNull()) {
                return new WhereClause("");
            } else {
                throw new WrongWhereTypeParamSizeException(w.name(), w.whereType(), 1);
            }
        }
        var columnName = columnNameParser.parseColumnName(w);
        String v1;
        Object[] whereParams;
        //针对 参数类型是 AbstractPlaceholderSQL 的情况进行特殊处理 下同
        if (w.value1() instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            v1 = "?";
            whereParams = new Object[]{w.value1()};
        }
        var whereClause = columnName + " " + getWhereKeyWord(w.whereType()) + " " + v1;
        return new WhereClause(whereClause, whereParams);
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
        var columnName = columnNameParser.parseColumnName(w);
        String v1;
        Object[] whereParams;
        if (w.value1() instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            v1 = "?";
            whereParams = new Object[]{w.value1()};
        }
        var whereClause = columnName + " " + getWhereKeyWord(w.whereType()) + " CONCAT('%'," + v1 + ",'%')";
        return new WhereClause(whereClause, whereParams);
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
        String v1;
        Object[] whereParams;
        if (w.value1() instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {

            var v = new Object[]{};
            try {
                v = toObjectArray(w.value1());
            } catch (Exception e) {
                throw new WrongWhereParamTypeException(w.name(), w.whereType(), "数组");
            }

            //移除空值并去重
            whereParams = Arrays.stream(v).filter(Objects::nonNull).distinct().toArray();
            //长度为空是抛异常
            if (whereParams.length == 0) {
                if (w.info().skipIfEmptyList()) {
                    return new WhereClause("");
                } else {
                    throw new ValidParamListIsEmptyException(w.name(), w.whereType());
                }
            }
            v1 = "(" + StringUtils.repeat("?", ", ", whereParams.length) + ")";
        }
        var whereClause = columnName + " " + getWhereKeyWord(w.whereType()) + " " + v1;
        return new WhereClause(whereClause, whereParams);
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
        var whereClause = columnName + " " + getWhereKeyWord(w.whereType()) + " " + v1 + " AND " + v2;
        return new WhereClause(whereClause, whereParams.toArray());
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
        var whereClause = getWhereKeyWord(w.whereType()) + "(" + columnName;
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

}
