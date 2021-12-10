package cool.scx.sql.where;

import cool.scx.util.CaseUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * where 封装体
 */
public final class WhereBody {

    /**
     * a
     */
    private final String name;

    /**
     * a
     */
    private final String whereClause;

    /**
     * 根据 where 条件对象生成的 map 防止 sql 注入
     */
    private final Map<String, Object> whereParamMap = new HashMap<>();

    /**
     *
     */
    WhereBody(final String _name, final WhereType whereType, final Object value1, final Object value2, boolean useOriginalName) {
        this.name = _name.trim();
        var columnName = useOriginalName ? this.name : CaseUtils.toSnake(this.name);
        var keyWord = whereType.keyWord();
        switch (whereType) {
            case IS_NULL, IS_NOT_NULL -> whereClause = columnName + " " + keyWord;
            case EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LIKE_REGEX, NOT_LIKE_REGEX -> {
                var placeholder = getPlaceholder(this.name);
                whereParamMap.put(placeholder, value1);
                whereClause = columnName + " " + keyWord + " :" + placeholder;
            }
            case JSON_CONTAINS -> {
                var jsonContainsParams = toArray(value1);
                var placeholder = getPlaceholder(this.name);
                whereParamMap.put(placeholder, jsonContainsParams);
                whereClause = keyWord + "(" + columnName + ", :" + placeholder + ")";
            }
            case LIKE, NOT_LIKE -> {
                var placeholder = getPlaceholder(this.name);
                whereParamMap.put(placeholder, value1);
                whereClause = columnName + " " + keyWord + " CONCAT('%',:" + placeholder + ",'%')";
            }
            case IN, NOT_IN -> {
                var inParams = toArray(value1);
                var sList = new String[inParams.length];
                for (int i = 0; i < inParams.length; i++) {
                    var placeholder = getPlaceholder(this.name);
                    sList[i] = ":" + placeholder;
                    whereParamMap.put(placeholder, inParams[i]);
                }
                whereClause = columnName + " " + keyWord + " (" + String.join(", ", sList) + ")";
            }
            case BETWEEN, NOT_BETWEEN -> {
                var placeholder1 = getPlaceholder(this.name);
                var placeholder2 = getPlaceholder(this.name);
                whereParamMap.put(placeholder1, value1);
                whereParamMap.put(placeholder2, value2);
                whereClause = columnName + " " + keyWord + " :" + placeholder1 + " AND :" + placeholder2;
            }
            default -> whereClause = null;
        }
    }

    /**
     * 获取占位符
     *
     * @param name n
     * @return a {@link java.lang.String} object
     */
    private static String getPlaceholder(String name) {
        return name + "_" + RandomUtils.getRandomString(6, true);
    }

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

    /**
     * a
     *
     * @return a
     */
    String name() {
        return name;
    }

    /**
     * a
     *
     * @return a
     */
    String whereClause() {
        return whereClause;
    }

    /**
     * a
     *
     * @return a
     */
    Map<String, Object> whereParamMap() {
        return whereParamMap;
    }

}