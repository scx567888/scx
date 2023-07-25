package cool.scx.data.query;

import cool.scx.data.Query0;
import cool.scx.data.query.exception.WrongWhereTypeParamSizeException;

import static cool.scx.data.query.Where.where;
import static cool.scx.data.query.WhereType.*;
import static cool.scx.util.StringUtils.isBlank;

/**
 * where 封装体
 *
 * @param name a
 * @author scx567888
 * @version 0.0.1
 */
public record WhereBody(String name, WhereType whereType, Object value1, Object value2,
                        WhereOption.Info info) implements Query0 {

    public WhereBody(String name, WhereType whereType, Object value1, Object value2, WhereOption.Info info) {
        //名称不能为空
        if (isBlank(name)) {
            throw new IllegalArgumentException("Where 参数错误 : 名称 不能为空 !!!");
        }
        //类型也不能为空
        if (whereType == null) {
            throw new IllegalArgumentException("Where 参数错误 : whereType 不能为空 !!!");
        }
        //校验参数 并获取有效的参数数量(不为空的) 每检测到一个有效的(不为空的) 便加 1
        var validParamSize = getValidParamSize(value1, value2);
        //类型所需的参数数量和所传的合法参数数量必须一致
        if (whereType.paramSize() != validParamSize) {
            throw new WrongWhereTypeParamSizeException(whereType);
        }
        this.name = name.trim();
        this.whereType = whereType;
        this.value1 = value1;
        this.value2 = value2;
        this.info = info;
    }

    /**
     * 为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody isNull(String fieldName, WhereOption... options) {
        return new WhereBody(fieldName, IS_NULL, null, null, new WhereOption.Info(options));
    }

    /**
     * 不为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public static WhereBody isNotNull(String fieldName, WhereOption... options) {
        return new WhereBody(fieldName, IS_NOT_NULL, null, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, EQUAL, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, NOT_EQUAL, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, GREATER_THAN, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, GREATER_THAN_OR_EQUAL, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, LESS_THAN, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, LESS_THAN_OR_EQUAL, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, BETWEEN, value1, value2, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, NOT_BETWEEN, value1, value2, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, LIKE_REGEX, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, NOT_LIKE_REGEX, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, LIKE, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, NOT_LIKE, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, JSON_CONTAINS, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, IN, value, null, new WhereOption.Info(options));
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
        return new WhereBody(fieldName, NOT_IN, value, null, new WhereOption.Info(options));
    }

    /**
     * 校验参数 并获取有效的参数数量(不为空的) 每检测到一个有效的(不为空的) 便加 1
     *
     * @param value1 a
     * @param value2 a
     * @return a
     */
    private static int getValidParamSize(Object value1, Object value2) {
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

    @Override
    public Where getWhere() {
        return where(this);
    }

}
