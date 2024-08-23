package cool.scx.data.query;

import cool.scx.data.Query;
import cool.scx.data.query.exception.ValidParamListIsEmptyException;
import cool.scx.data.query.exception.WrongWhereTypeParamSizeException;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.data.query.WhereType.*;

/**
 * where 查询条件封装类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class WhereBodySet extends QueryLike<WhereBodySet> implements Logic {

    /**
     * 存储查询条件 key 为 fieldName ,采用 map 而不是 list 是为了保证重复添加的会直接覆盖
     */
    private final List<WhereBody> whereBodyList;

    private final LogicType type;

    /**
     * 创建一个 Where 对象
     */
    public WhereBodySet(LogicType type) {
        this.type = type;
        this.whereBodyList = new ArrayList<>();
    }

    /**
     * 添加一个查询条件
     *
     * @param name      名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param whereType where 类型
     * @param value1    参数1
     * @param value2    参数2
     * @param options   配置
     * @return 本身 , 方便链式调用
     */
    public WhereBodySet add2(String name, WhereType whereType, Object value1, Object value2, WhereOption... options) {
        return _add(name, whereType, value1, value2, 2, options);
    }

    /**
     * 添加一个查询条件
     *
     * @param name      名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param whereType where 类型
     * @param value1    参数1
     * @param options   配置
     * @return 本身 , 方便链式调用
     */
    public WhereBodySet add1(String name, WhereType whereType, Object value1, WhereOption... options) {
        return _add(name, whereType, value1, null, 1, options);
    }

    /**
     * 添加一个查询条件
     *
     * @param name      名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param whereType where 类型
     * @param options   配置
     * @return 本身 , 方便链式调用
     */
    public WhereBodySet add0(String name, WhereType whereType, WhereOption... options) {
        return _add(name, whereType, null, null, 0, options);
    }

    /**
     * 查询条件是否为空
     *
     * @return a boolean
     */
    public boolean isEmpty() {
        return whereBodyList.size() == 0;
    }

    /**
     * 为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet isNull(String fieldName, WhereOption... options) {
        return add0(fieldName, IS_NULL, options);
    }

    /**
     * 不为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet isNotNull(String fieldName, WhereOption... options) {
        return add0(fieldName, IS_NOT_NULL, options);
    }

    /**
     * 相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet eq(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, EQUAL, value, options);
    }

    /**
     * 不相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet ne(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, NOT_EQUAL, value, options);
    }

    /**
     * 大于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet gt(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, GREATER_THAN, value, options);
    }

    /**
     * 大于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet ge(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, GREATER_THAN_OR_EQUAL, value, options);
    }

    /**
     * 小于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet lt(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, LESS_THAN, value, options);
    }

    /**
     * 小于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet le(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, LESS_THAN_OR_EQUAL, value, options);
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
    public WhereBodySet between(String fieldName, Object value1, Object value2, WhereOption... options) {
        return add2(fieldName, BETWEEN, value1, value2, options);
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
    public WhereBodySet notBetween(String fieldName, Object value1, Object value2, WhereOption... options) {
        return add2(fieldName, NOT_BETWEEN, value1, value2, options);
    }

    /**
     * like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet likeRegex(String fieldName, String value, WhereOption... options) {
        return add1(fieldName, LIKE_REGEX, value, options);
    }

    /**
     * not like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet notLikeRegex(String fieldName, String value, WhereOption... options) {
        return add1(fieldName, NOT_LIKE_REGEX, value, options);
    }

    /**
     * like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     参数 默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet like(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, LIKE, value, options);
    }

    /**
     * not like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet notLike(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, NOT_LIKE, value, options);
    }

    /**
     * 包含  : 一般用于 JSON 格式字段 区别于 in
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet jsonContains(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, JSON_CONTAINS, value, options);
    }

    /**
     * 在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet in(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, IN, value, options);
    }

    /**
     * 不在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public WhereBodySet notIn(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, NOT_IN, value, options);
    }

    /**
     * a
     *
     * @param name          a
     * @param whereType     a
     * @param value1        a
     * @param value2        a
     * @param options       a
     * @param needParamSize a int
     * @return a
     */
    private WhereBodySet _add(String name, WhereType whereType, Object value1, Object value2, int needParamSize, WhereOption... options) {
        //创建 option 信息
        var info = new WhereOption.Info(options);
        try {
            var whereBody = new WhereBody(name, whereType, value1, value2, info);
            //类型所需的参数数量和所传的参数数量必须一致
            if (whereType.paramSize() != needParamSize) {
                throw new IllegalArgumentException("Where 参数错误 : whereType 类型 : " + whereType + " , 参数数量必须为 " + whereType.paramSize());
            }
            // 是否替换
            if (info.replace()) {
                whereBodyList.removeIf(w -> whereBody.name().equals(w.name()));
            }
            whereBodyList.add(whereBody);
        } catch (WrongWhereTypeParamSizeException e) {
            if (!info.skipIfNull()) {
                throw e;
            }
        } catch (ValidParamListIsEmptyException e) {
            if (!info.skipIfEmptyList()) {
                throw e;
            }
        }
        return this;
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public WhereBodySet remove(String name) {
        whereBodyList.removeIf(w -> w.name().equals(name.trim()));
        return this;
    }

    /**
     * 清除所有 where 条件 (不包括 whereSQL)
     *
     * @return this 方便链式调用
     */
    public WhereBodySet clear() {
        whereBodyList.clear();
        return this;
    }

    @Override
    public LogicType type() {
        return this.type;
    }

    @Override
    public Object[] clauses() {
        return whereBodyList.toArray();
    }

    @Override
    public Query toQuery() {
        return new QueryImpl().where(this);
    }

}
