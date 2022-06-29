package cool.scx.sql.where;

import cool.scx.sql.AbstractPlaceholderSQL;
import cool.scx.sql.SQLHelper;
import cool.scx.sql.exception.ValidParamListIsEmptyException;
import cool.scx.util.CaseUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;
import cool.scx.util.tuple.Tuple2;
import cool.scx.util.tuple.Tuples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

interface WhereTypeHandler {

    WhereTypeHandler IS_NULL_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = new Object[]{};
        var whereClause = columnName + " " + whereType.keyWord();
        return Tuples.of(whereParams, whereClause);
    };

    /**
     * 相同的实现 我们使用相同的方法
     */
    WhereTypeHandler IS_NOT_NULL_HANDLER = IS_NULL_HANDLER;

    WhereTypeHandler EQUAL_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        Object[] whereParams;
        //针对 参数类型是 AbstractPlaceholderSQL 的情况进行特殊处理 下同
        if (value1 instanceof AbstractPlaceholderSQL<?> a) {
            v1 = "(" + a.normalSQL() + ")";
            whereParams = a.objectArrayParams();
        } else {
            v1 = "?";
            whereParams = new Object[]{value1};
        }
        var whereClause = columnName + " " + whereType.keyWord() + " " + v1;
        return Tuples.of(whereParams, whereClause);
    };

    WhereTypeHandler NOT_EQUAL_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler LESS_THAN_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler LESS_THAN_OR_EQUAL_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler GREATER_THAN_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler GREATER_THAN_OR_EQUAL_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler LIKE_REGEX_HANDLER = EQUAL_HANDLER;
    WhereTypeHandler NOT_LIKE_REGEX_HANDLER = EQUAL_HANDLER;

    WhereTypeHandler LIKE_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        Object[] whereParams;
        if (value1 instanceof AbstractPlaceholderSQL<?> a) {
            v1 = "(" + a.normalSQL() + ")";
            whereParams = a.objectArrayParams();
        } else {
            v1 = "?";
            whereParams = new Object[]{value1};
        }
        var whereClause = columnName + " " + whereType.keyWord() + " CONCAT('%'," + v1 + ",'%')";
        return Tuples.of(whereParams, whereClause);
    };

    WhereTypeHandler NOT_LIKE_HANDLER = LIKE_HANDLER;

    WhereTypeHandler IN_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        Object[] whereParams;
        if (value1 instanceof AbstractPlaceholderSQL<?> a) {
            v1 = "(" + a.normalSQL() + ")";
            whereParams = a.objectArrayParams();
        } else {
            //移除空值并去重
            whereParams = Arrays.stream(toArray(value1)).filter(Objects::nonNull).distinct().toArray();
            //长度为空是抛异常
            if (whereParams.length == 0) {
                throw new ValidParamListIsEmptyException(whereType);
            }
            v1 = "(" + StringUtils.repeat("?", ", ", whereParams.length) + ")";
        }
        var whereClause = columnName + " " + whereType.keyWord() + " " + v1;
        return Tuples.of(whereParams, whereClause);
    };

    WhereTypeHandler NOT_IN_HANDLER = IN_HANDLER;

    WhereTypeHandler BETWEEN_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        String v1;
        String v2;
        var whereParams = new ArrayList<>();
        if (value1 instanceof AbstractPlaceholderSQL<?> a) {
            v1 = "(" + a.normalSQL() + ")";
            Collections.addAll(whereParams, a.objectArrayParams());
        } else {
            v1 = "?";
            whereParams.add(value1);
        }
        if (value2 instanceof AbstractPlaceholderSQL<?> a) {
            v2 = "(" + a.normalSQL() + ")";
            Collections.addAll(whereParams, a.objectArrayParams());
        } else {
            v2 = "?";
            whereParams.add(value2);
        }
        var whereClause = columnName + " " + whereType.keyWord() + " " + v1 + " AND " + v2;
        return Tuples.of(whereParams.toArray(), whereClause);
    };

    WhereTypeHandler NOT_BETWEEN_HANDLER = BETWEEN_HANDLER;

    WhereTypeHandler JSON_CONTAINS_HANDLER = (name, whereType, value1, value2, info) -> {
        var c = SQLHelper.splitIntoColumnNameAndFieldPath(name);
        var columnName = info.useOriginalName() ? c.value0() : CaseUtils.toSnake(c.value0());
        if (StringUtils.isBlank(c.value0())) {
            throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询名称不合法 !!! 字段名 : " + name);
        }
        String v1;
        Object[] whereParams;
        if (value1 instanceof AbstractPlaceholderSQL<?> a) {
            v1 = "(" + a.normalSQL() + ")";
            whereParams = a.objectArrayParams();
        } else {
            v1 = "?";
            var jsonContainsParams = toArray(value1);
            whereParams = new Object[]{jsonContainsParams};
        }
        var whereClause = whereType.keyWord() + "(" + columnName;
        if (StringUtils.notBlank(c.value1())) {
            whereClause = whereClause + ", " + v1 + ", '$" + c.value1() + "')";
        } else {
            whereClause = whereClause + ", " + v1 + ")";
        }
        return Tuples.of(whereParams, whereClause);
    };

    /**
     * a
     *
     * @param value a
     * @return a
     */
    private static Object[] toArray(Object value) {
        if (value instanceof String str) {
            return str.split(",");
        }
        return ObjectUtils.toObjectArray(value);
    }

    private static boolean hasNull(Object[] value) {
        for (var o : value) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    Tuple2<Object[], String> getWhereParamsAndWhereClause(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info);

}
