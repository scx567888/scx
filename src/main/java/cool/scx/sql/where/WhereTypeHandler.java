package cool.scx.sql.where;

import cool.scx.sql.SQLHelper;
import cool.scx.util.CaseUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;
import cool.scx.util.tuple.Tuple2;
import cool.scx.util.tuple.Tuples;

import java.util.Arrays;

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
        var whereParams = new Object[]{value1};
        var whereClause = columnName + " " + whereType.keyWord() + " ?";
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
        var whereParams = new Object[]{value1};
        var whereClause = columnName + " " + whereType.keyWord() + " CONCAT('%',?,'%')";
        return Tuples.of(whereParams, whereClause);
    };

    WhereTypeHandler NOT_LIKE_HANDLER = LIKE_HANDLER;

    WhereTypeHandler IN_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = toArray(value1);
        var sList = new String[whereParams.length];
        Arrays.fill(sList, "?");
        var whereClause = columnName + " " + whereType.keyWord() + " (" + String.join(", ", sList) + ")";
        return Tuples.of(whereParams, whereClause);
    };

    WhereTypeHandler NOT_IN_HANDLER = IN_HANDLER;

    WhereTypeHandler BETWEEN_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = new Object[]{value1, value2};
        var whereClause = columnName + " " + whereType.keyWord() + " ? AND ?";
        return Tuples.of(whereParams, whereClause);
    };

    WhereTypeHandler NOT_BETWEEN_HANDLER = BETWEEN_HANDLER;

    WhereTypeHandler JSON_CONTAINS_HANDLER = (name, whereType, value1, value2, info) -> {
        var c = SQLHelper.splitIntoColumnNameAndFieldPath(name);
        var columnName = info.useOriginalName() ? c.value0() : CaseUtils.toSnake(c.value0());
        if (StringUtils.isNotBlank(c.value0())) {
            var jsonContainsParams = toArray(value1);
            var whereParams = new Object[]{jsonContainsParams};
            var whereClause = whereType.keyWord() + "(" + columnName;
            if (StringUtils.isNotBlank(c.value1())) {
                whereClause = whereClause + ", ?, '$" + c.value1() + "')";
            } else {
                whereClause = whereClause + ", ?)";
            }
            return Tuples.of(whereParams, whereClause);
        } else {
            throw new IllegalArgumentException("使用 JSON_CONTAINS 时, 查询名称不合法 !!! 字段名 : " + name);
        }
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

    Tuple2<Object[], String> getWhereParamsAndWhereClause(String name, WhereType whereType, Object value1, Object value2, WhereOptionInfo info);

}
