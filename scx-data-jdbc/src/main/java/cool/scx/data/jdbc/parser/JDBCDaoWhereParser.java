package cool.scx.data.jdbc.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.common.util.ObjectUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.data.query.WhereClause;
import cool.scx.data.query.WhereOption.Info;
import cool.scx.data.query.WhereType;
import cool.scx.data.query.exception.ValidParamListIsEmptyException;
import cool.scx.data.query.parser.WhereParser;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sql.SQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static cool.scx.common.util.ArrayUtils.toObjectArray;
import static cool.scx.common.util.ObjectUtils.toJson;
import static cool.scx.data.jdbc.parser.ColumnNameParser.parseColumnName;
import static cool.scx.data.jdbc.parser.ColumnNameParser.splitIntoColumnNameAndFieldPath;
import static java.util.Collections.addAll;

public class JDBCDaoWhereParser extends WhereParser {

    private final AnnotationConfigTable tableInfo;
    private final Dialect dialect;

    public JDBCDaoWhereParser(AnnotationConfigTable tableInfo, Dialect dialect) {
        this.tableInfo = tableInfo;
        this.dialect = dialect;
    }

    @Override
    public WhereClause parseIsNull(String name, WhereType whereType, Object value1, Object value2, Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = new Object[]{};
        var whereClause = columnName + " " + getWhereKeyWord(whereType);
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseEqual(String name, WhereType whereType, Object value1, Object value2, Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        Object[] whereParams;
        //针对 参数类型是 AbstractPlaceholderSQL 的情况进行特殊处理 下同
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            v1 = "?";
            whereParams = new Object[]{value1};
        }
        var whereClause = columnName + " " + getWhereKeyWord(whereType) + " " + v1;
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseLike(String name, WhereType whereType, Object value1, Object value2, Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        Object[] whereParams;
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            v1 = "?";
            whereParams = new Object[]{value1};
        }
        var whereClause = columnName + " " + getWhereKeyWord(whereType) + " CONCAT('%'," + v1 + ",'%')";
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseIn(String name, WhereType whereType, Object value1, Object value2, Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        Object[] whereParams;
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            //移除空值并去重
            whereParams = Arrays.stream(toObjectArray(value1)).filter(Objects::nonNull).distinct().toArray();
            //长度为空是抛异常
            if (whereParams.length == 0) {
                throw new ValidParamListIsEmptyException(whereType);
            }
            v1 = "(" + StringUtils.repeat("?", ", ", whereParams.length) + ")";
        }
        var whereClause = columnName + " " + getWhereKeyWord(whereType) + " " + v1;
        return new WhereClause(whereClause, whereParams);
    }

    @Override
    public WhereClause parseBetween(String name, WhereType whereType, Object value1, Object value2, Info info) {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        String v2;
        var whereParams = new ArrayList<>();
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            addAll(whereParams, a.params());
        } else {
            v1 = "?";
            whereParams.add(value1);
        }
        if (value2 instanceof SQL a) {
            v2 = "(" + a.sql() + ")";
            addAll(whereParams, a.params());
        } else {
            v2 = "?";
            whereParams.add(value2);
        }
        var whereClause = columnName + " " + getWhereKeyWord(whereType) + " " + v1 + " AND " + v2;
        return new WhereClause(whereClause, whereParams.toArray());
    }

    @Override
    public WhereClause parseJsonContains(String name, WhereType whereType, Object value1, Object value2, Info info) {
        var c = splitIntoColumnNameAndFieldPath(name);
        var columnName = info.useOriginalName() ? c.columnName() : tableInfo.getColumn(c.columnName()).name();
        if (StringUtils.isBlank(c.columnName())) {
            throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询名称不合法 !!! 字段名 : " + name);
        }
        String v1;
        Object[] whereParams;
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            whereParams = a.params();
        } else {
            v1 = "?";
            if (info.useOriginalValue()) {
                whereParams = new Object[]{value1};
            } else {
                try {
                    whereParams = new Object[]{toJson(value1, new ObjectUtils.Options().setIgnoreJsonIgnore(true).setIgnoreNullValue(true))};
                } catch (JsonProcessingException e) {
                    throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询参数不合法(无法正确转换为 JSON) !!! 字段名 : " + name, e);
                }
            }
        }
        var whereClause = getWhereKeyWord(whereType) + "(" + columnName;
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

    public final WhereClause parseSQL(SQL sql) {
        return new WhereClause("(" + sql.sql() + ")", sql.params());
    }

}
