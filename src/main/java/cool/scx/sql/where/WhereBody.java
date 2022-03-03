package cool.scx.sql.where;

import cool.scx.util.CaseUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * where 封装体
 *
 * @author scx567888
 * @version 1.11.8
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
    private final Object[] whereParams;

    /**
     * <p>Constructor for WhereBody.</p>
     *
     * @param _name           a {@link java.lang.String} object
     * @param whereType       a {@link cool.scx.sql.where.WhereType} object
     * @param value1          a {@link java.lang.Object} object
     * @param value2          a {@link java.lang.Object} object
     * @param useOriginalName a boolean
     */
    WhereBody(final String _name, final WhereType whereType, final Object value1, final Object value2, boolean useOriginalName) {
        this.name = _name.trim();
        var columnName = useOriginalName ? this.name : CaseUtils.toSnake(this.name);
        var keyWord = whereType.keyWord();
        switch (whereType) {
            case IS_NULL, IS_NOT_NULL -> {
                whereParams = new Object[]{};
                whereClause = columnName + " " + keyWord;
            }
            case EQUAL, NOT_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LIKE_REGEX, NOT_LIKE_REGEX -> {
                whereParams = new Object[]{value1};
                whereClause = columnName + " " + keyWord + " ?";
            }
            case JSON_CONTAINS -> {
                var jsonContainsParams = toArray(value1);
                whereParams = new Object[]{jsonContainsParams};
                whereClause = keyWord + "(" + columnName + ", ?)";
            }
            case LIKE, NOT_LIKE -> {
                whereParams = new Object[]{value1};
                whereClause = columnName + " " + keyWord + " CONCAT('%',?,'%')";
            }
            case IN, NOT_IN -> {
                whereParams = toArray(value1);
                var sList = new String[whereParams.length];
                for (int i = 0; i < whereParams.length; i++) {
                    sList[i] = "?";
                }
                whereClause = columnName + " " + keyWord + " (" + String.join(", ", sList) + ")";
            }
            case BETWEEN, NOT_BETWEEN -> {
                whereParams = new Object[]{value1, value2};
                whereClause = columnName + " " + keyWord + " ? AND ?";
            }
            default -> {
                whereParams = null;
                whereClause = null;
            }
        }
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
     * 为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody isNull(String fieldName, WhereOption... options) {
        return newInstance0(fieldName, WhereType.IS_NULL, options);
    }

    /**
     * 不为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody isNotNull(String fieldName, WhereOption... options) {
        return newInstance0(fieldName, WhereType.IS_NOT_NULL, options);
    }

    /**
     * 相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody equal(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.EQUAL, value, options);
    }

    /**
     * 不相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notEqual(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.NOT_EQUAL, value, options);
    }

    /**
     * 大于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody greaterThan(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.GREATER_THAN, value, options);
    }

    /**
     * 大于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody greaterThanOrEqual(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.GREATER_THAN_OR_EQUAL, value, options);
    }

    /**
     * 小于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody lessThan(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.LESS_THAN, value, options);
    }

    /**
     * 小于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody lessThanOrEqual(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.LESS_THAN_OR_EQUAL, value, options);
    }

    /**
     * 两者之间
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value1    比较值1
     * @param value2    比较值2
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody between(String fieldName, Object value1, Object value2, WhereOption... options) {
        return newInstance2(fieldName, WhereType.BETWEEN, value1, value2, options);
    }

    /**
     * 不处于两者之间
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value1    比较值1
     * @param value2    比较值2
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notBetween(String fieldName, Object value1, Object value2, WhereOption... options) {
        return newInstance2(fieldName, WhereType.NOT_BETWEEN, value1, value2, options);
    }

    /**
     * like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody likeRegex(String fieldName, String value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.LIKE_REGEX, value, options);
    }

    /**
     * not like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notLikeRegex(String fieldName, String value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.NOT_LIKE_REGEX, value, options);
    }

    /**
     * like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     参数 默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody like(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.LIKE, value, options);
    }

    /**
     * not like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notLike(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.NOT_LIKE, value, options);
    }

    /**
     * 包含  : 一般用于 JSON 格式字段 区别于 in
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody jsonContains(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.JSON_CONTAINS, value, options);
    }

    /**
     * 在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody in(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.IN, value, options);
    }

    /**
     * 不在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody notIn(String fieldName, Object value, WhereOption... options) {
        return newInstance1(fieldName, WhereType.NOT_IN, value, options);
    }

    /**
     * <p>_newInstance.</p>
     *
     * @param name          a {@link java.lang.String} object
     * @param whereType     a {@link cool.scx.sql.where.WhereType} object
     * @param value1        a {@link java.lang.Object} object
     * @param value2        a {@link java.lang.Object} object
     * @param needParamSize a int
     * @param options       a {@link cool.scx.sql.where.WhereOption} object
     * @return a {@link cool.scx.sql.where.WhereBody} object
     */
    private static WhereBody _newInstance(String name, WhereType whereType, Object value1, Object value2, int needParamSize, WhereOption... options) {
        var useOriginalName = false;// 是否使用原始名称
        for (var option : options) {
            if (option == WhereOption.USE_ORIGINAL_NAME) {
                useOriginalName = true;
                break;
            }
        }
        //校验参数 并获取有效的参数数量(不为空的) 每检测到一个有效的(不为空的) 便加 1
        var validParamSize = checkParamsAndGetValidParamSize(name, whereType, value1, value2, needParamSize);
        //有效参数的数量和所需的参数数量不一致
        if (whereType.paramSize() != validParamSize) {
            //根据是否跳过空进行校验
            throw new IllegalArgumentException("Where 参数错误 : whereType 类型 : " + whereType + " , 参数列表不能为空 !!!");
        }
        // 是否使用原始名称 (即不进行转义)
        return new WhereBody(name, whereType, value1, value2, useOriginalName);
    }

    /**
     * 创建一个查询条件
     *
     * @param name      名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param whereType WhereBody类型
     * @param value1    参数1
     * @param value2    参数2
     * @param options   具体配置
     * @return 本身 , 方便链式调用
     */
    public static WhereBody newInstance2(String name, WhereType whereType, Object value1, Object value2, WhereOption... options) {
        return _newInstance(name, whereType, value1, value2, 2, options);
    }

    /**
     * 创建一个查询条件
     *
     * @param name      名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param whereType WhereBody类型
     * @param value1    参数1
     * @param options   配置
     * @return 本身 , 方便链式调用
     */
    public static WhereBody newInstance1(String name, WhereType whereType, Object value1, WhereOption... options) {
        return _newInstance(name, whereType, value1, null, 1, options);
    }

    /**
     * 创建一个查询条件
     *
     * @param name      名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param whereType WhereBody类型
     * @param options   配置
     * @return 本身 , 方便链式调用
     */
    public static WhereBody newInstance0(String name, WhereType whereType, WhereOption... options) {
        return _newInstance(name, whereType, null, null, 0, options);
    }

    /**
     * 校验参数 并获取有效的参数数量(不为空的) 每检测到一个有效的(不为空的) 便加 1
     *
     * @param name          a
     * @param whereType     a
     * @param value1        a
     * @param value2        a
     * @param needParamSize a
     * @return a
     */
    static int checkParamsAndGetValidParamSize(String name, WhereType whereType, Object value1, Object value2, int needParamSize) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Where 参数错误 : 名称 不能为空 !!!");
        }
        //类型也不能为空
        if (whereType == null) {
            throw new IllegalArgumentException("Where 参数错误 : whereType 不能为空 !!!");
        }
        //类型所需的参数数量和所传的参数数量必须一致
        if (whereType.paramSize() != needParamSize) {
            throw new IllegalArgumentException("Where 参数错误 : whereType 类型 : " + whereType + " , 参数数量必须为 " + whereType.paramSize());
        }
        //有效的参数数量(不为空的) 每检测到一个有效的(不为空的) 便加 1
        var validParamSize = 0;
        if (value1 != null) {
            validParamSize = validParamSize + 1;
        }
        if (value2 != null) {
            validParamSize = validParamSize + 1;
        }
        return validParamSize;
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
    Object[] whereParams() {
        return whereParams;
    }

}
