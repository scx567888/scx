package cool.scx.sql.where;

import cool.scx.sql.SQLHelper;
import cool.scx.tuple.ScxTuple;
import cool.scx.tuple.Tuple2;
import cool.scx.util.CaseUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.util.List;
import java.util.Set;

interface WhereTypeHandler {

    WhereTypeHandler IS_NULL_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = new Object[]{};
        var whereClause = columnName + " " + whereType.keyWord();
        return ScxTuple.of(whereParams, whereClause);
    };

    /**
     * 相同的实现 我们使用相同的方法
     */
    WhereTypeHandler IS_NOT_NULL_HANDLER = IS_NULL_HANDLER;

    WhereTypeHandler EQUAL_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = new Object[]{value1};
        var whereClause = columnName + " " + whereType.keyWord() + " ?";
        return ScxTuple.of(whereParams, whereClause);
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
        return ScxTuple.of(whereParams, whereClause);
    };

    WhereTypeHandler NOT_LIKE_HANDLER = LIKE_HANDLER;

    WhereTypeHandler IN_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = toArray(value1);
        var sList = new String[whereParams.length];
        for (int i = 0; i < whereParams.length; i++) {
            sList[i] = "?";
        }
        var whereClause = columnName + " " + whereType.keyWord() + " (" + String.join(", ", sList) + ")";
        return ScxTuple.of(whereParams, whereClause);
    };

    WhereTypeHandler NOT_IN_HANDLER = IN_HANDLER;

    WhereTypeHandler BETWEEN_HANDLER = (name, whereType, value1, value2, info) -> {
        var columnName = SQLHelper.getColumnName(name, info.useJsonExtract(), info.useOriginalName());
        var whereParams = new Object[]{value1, value2};
        var whereClause = columnName + " " + whereType.keyWord() + " ? AND ?";
        return ScxTuple.of(whereParams, whereClause);
    };

    WhereTypeHandler NOT_BETWEEN_HANDLER = BETWEEN_HANDLER;

    WhereTypeHandler JSON_CONTAINS_HANDLER = (name, whereType, value1, value2, info) -> {
        var c = SQLHelper.splitIntoColumnNameAndFieldPath(name);
        var columnName = info.useOriginalName() ? c.columnName() : CaseUtils.toSnake(c.columnName());
        if (StringUtils.isNotBlank(c.columnName())) {
            var jsonContainsParams = toArray(value1);
            var whereParams = new Object[]{jsonContainsParams};
            var whereClause = "";
            if (StringUtils.isNotBlank(c.fieldPath())) {
                whereClause = whereType.keyWord() + "(" + columnName + ", ?, '$" + c.fieldPath() + "')";
            } else {
                whereClause = whereType.keyWord() + "(" + columnName + ", ?)";
            }
            return ScxTuple.of(whereParams, whereClause);
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
        var objectArray = new Object[0];
        if (value.getClass().isArray() || value instanceof List || value instanceof Set) {
            objectArray = ObjectUtils.convertValue(value, objectArray.getClass());
        } else if (value instanceof String) {
            objectArray = ((String) value).split(",");
        } else {
            objectArray = new Object[]{value};
        }
        return objectArray;
    }

    Tuple2<Object[], String> getWhereParamsAndWhereClause(String name, WhereType whereType, Object value1, Object value2, WhereOptionInfo info);

}
