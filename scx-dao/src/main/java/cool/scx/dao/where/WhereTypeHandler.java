package cool.scx.dao.where;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.dao.mapping.TableInfo;
import cool.scx.dao.where.exception.ValidParamListIsEmptyException;
import cool.scx.sql.SQL;
import cool.scx.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static cool.scx.dao.ColumnNameParser.parseColumnName;
import static cool.scx.dao.ColumnNameParser.splitIntoColumnNameAndFieldPath;
import static cool.scx.util.ObjectUtils.*;

/**
 * WhereTypeHandler
 *
 * @author scx567888
 * @version 0.0.1
 */
interface WhereTypeHandler {

    WhereTypeHandler IS_NULL_HANDLER = (tableInfo, name, whereType, value1, value2, info) -> {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = new Object[]{};
        var whereClause = columnName + " " + whereType.keyWord();
        return new WhereParamsAndWhereClause(whereParams, whereClause);
    };

    /**
     * 相同的实现 我们使用相同的方法
     */
    WhereTypeHandler IS_NOT_NULL_HANDLER = IS_NULL_HANDLER;

    WhereTypeHandler EQUAL_HANDLER = (tableInfo, name, whereType, value1, value2, info) -> {
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
        var whereClause = columnName + " " + whereType.keyWord() + " " + v1;
        return new WhereParamsAndWhereClause(whereParams, whereClause);
    };

    WhereTypeHandler NOT_EQUAL_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler LESS_THAN_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler LESS_THAN_OR_EQUAL_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler GREATER_THAN_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler GREATER_THAN_OR_EQUAL_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler LIKE_REGEX_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler NOT_LIKE_REGEX_HANDLER = EQUAL_HANDLER;

    WhereTypeHandler LIKE_HANDLER = (tableInfo, name, whereType, value1, value2, info) -> {
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
        var whereClause = columnName + " " + whereType.keyWord() + " CONCAT('%'," + v1 + ",'%')";
        return new WhereParamsAndWhereClause(whereParams, whereClause);
    };

    WhereTypeHandler NOT_LIKE_HANDLER = LIKE_HANDLER;

    WhereTypeHandler IN_HANDLER = (tableInfo, name, whereType, value1, value2, info) -> {
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
        var whereClause = columnName + " " + whereType.keyWord() + " " + v1;
        return new WhereParamsAndWhereClause(whereParams, whereClause);
    };

    WhereTypeHandler NOT_IN_HANDLER = IN_HANDLER;

    WhereTypeHandler BETWEEN_HANDLER = (tableInfo, name, whereType, value1, value2, info) -> {
        var columnName = parseColumnName(tableInfo, name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        String v2;
        var whereParams = new ArrayList<>();
        if (value1 instanceof SQL a) {
            v1 = "(" + a.sql() + ")";
            Collections.addAll(whereParams, a.params());
        } else {
            v1 = "?";
            whereParams.add(value1);
        }
        if (value2 instanceof SQL a) {
            v2 = "(" + a.sql() + ")";
            Collections.addAll(whereParams, a.params());
        } else {
            v2 = "?";
            whereParams.add(value2);
        }
        var whereClause = columnName + " " + whereType.keyWord() + " " + v1 + " AND " + v2;
        return new WhereParamsAndWhereClause(whereParams.toArray(), whereClause);
    };

    WhereTypeHandler NOT_BETWEEN_HANDLER = BETWEEN_HANDLER;

    WhereTypeHandler JSON_CONTAINS_HANDLER = (tableInfo, name, whereType, value1, value2, info) -> {
        var c = splitIntoColumnNameAndFieldPath(name);
        var columnName = info.useOriginalName() ? c.columnName() : tableInfo.getColumn(c.columnName()).columnName();
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
                    whereParams = new Object[]{toJson(value1, Option.IGNORE_JSON_IGNORE, Option.IGNORE_NULL_VALUE)};
                } catch (JsonProcessingException e) {
                    throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询参数不合法(无法正确转换为 JSON) !!! 字段名 : " + name, e);
                }
            }
        }
        var whereClause = whereType.keyWord() + "(" + columnName;
        if (StringUtils.notBlank(c.fieldPath())) {
            whereClause = whereClause + ", " + v1 + ", '$" + c.fieldPath() + "')";
        } else {
            whereClause = whereClause + ", " + v1 + ")";
        }
        return new WhereParamsAndWhereClause(whereParams, whereClause);
    };

    WhereParamsAndWhereClause getWhereParamsAndWhereClause(TableInfo<?> tableInfo, String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info);

}
