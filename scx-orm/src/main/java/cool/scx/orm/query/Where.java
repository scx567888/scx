package cool.scx.orm.query;

import cool.scx.orm.query.exception.ValidParamListIsEmptyException;
import cool.scx.orm.query.exception.WrongWhereTypeParamSizeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * where 查询条件封装类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Where {

    /**
     * 存储查询条件 key 为 fieldName ,采用 map 而不是 list 是为了保证重复添加的会直接覆盖
     */
    private final List<WhereBody> whereBodyList;

    /**
     * 自定义的查询语句
     */
    private Object[] whereSQL;

    /**
     * 创建一个 Where 对象
     */
    public Where() {
        this.whereBodyList = new ArrayList<>();
        this.whereSQL = new Object[0];
    }

    /**
     * 根据旧的 Where 创建一个 Where 对象
     *
     * @param oldWhere 旧的 Where
     */
    public Where(Where oldWhere) {
        this.whereBodyList = new ArrayList<>(oldWhere.whereBodyList);
        this.whereSQL = Arrays.copyOf(oldWhere.whereSQL, oldWhere.whereSQL.length);
    }

    /**
     * 添加一个查询条件 (注意 : 此处添加的所有条件都会以 and 拼接 , 如需使用 or 请考虑使用 {@link #whereSQL(Object...)} })
     *
     * @param name      名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param whereType where 类型
     * @param value1    参数1
     * @param value2    参数2
     * @param options   配置
     * @return 本身 , 方便链式调用
     */
    public Where add2(String name, WhereType whereType, Object value1, Object value2, WhereOption... options) {
        return _add(name, whereType, value1, value2, 2, options);
    }

    /**
     * 添加一个查询条件 (注意 : 此处添加的所有条件都会以 and 拼接 , 如需使用 or 请考虑使用 {@link #whereSQL(Object...)} })
     *
     * @param name      名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param whereType where 类型
     * @param value1    参数1
     * @param options   配置
     * @return 本身 , 方便链式调用
     */
    public Where add1(String name, WhereType whereType, Object value1, WhereOption... options) {
        return _add(name, whereType, value1, null, 1, options);
    }

    /**
     * 添加一个查询条件 (注意 : 此处添加的所有条件都会以 and 拼接 , 如需使用 or 请考虑使用 {@link #whereSQL(Object...)} })
     *
     * @param name      名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param whereType where 类型
     * @param options   配置
     * @return 本身 , 方便链式调用
     */
    public Where add0(String name, WhereType whereType, WhereOption... options) {
        return _add(name, whereType, null, null, 0, options);
    }

    /**
     * 查询条件是否为空
     *
     * @return a boolean
     */
    public boolean isEmpty() {
        return whereBodyList.size() == 0 && whereSQL == null;
    }

    /**
     * 为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where isNull(String fieldName, WhereOption... options) {
        return add0(fieldName, WhereType.IS_NULL, options);
    }

    /**
     * 不为空
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where isNotNull(String fieldName, WhereOption... options) {
        return add0(fieldName, WhereType.IS_NOT_NULL, options);
    }

    /**
     * 相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where equal(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.EQUAL, value, options);
    }

    /**
     * 不相等
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where notEqual(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.NOT_EQUAL, value, options);
    }

    /**
     * 大于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where greaterThan(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.GREATER_THAN, value, options);
    }

    /**
     * 大于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where greaterThanOrEqual(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.GREATER_THAN_OR_EQUAL, value, options);
    }

    /**
     * 小于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where lessThan(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.LESS_THAN, value, options);
    }

    /**
     * 小于等于
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where lessThanOrEqual(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.LESS_THAN_OR_EQUAL, value, options);
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
    public Where between(String fieldName, Object value1, Object value2, WhereOption... options) {
        return add2(fieldName, WhereType.BETWEEN, value1, value2, options);
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
    public Where notBetween(String fieldName, Object value1, Object value2, WhereOption... options) {
        return add2(fieldName, WhereType.NOT_BETWEEN, value1, value2, options);
    }

    /**
     * like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where likeRegex(String fieldName, String value, WhereOption... options) {
        return add1(fieldName, WhereType.LIKE_REGEX, value, options);
    }

    /**
     * not like : 根据 SQL 表达式进行判断
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     SQL 表达式
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where notLikeRegex(String fieldName, String value, WhereOption... options) {
        return add1(fieldName, WhereType.NOT_LIKE_REGEX, value, options);
    }

    /**
     * like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     参数 默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where like(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.LIKE, value, options);
    }

    /**
     * not like : 默认会在首尾添加 %
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     默认会在首尾添加 %
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where notLike(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.NOT_LIKE, value, options);
    }

    /**
     * 包含  : 一般用于 JSON 格式字段 区别于 in
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where jsonContains(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.JSON_CONTAINS, value, options);
    }

    /**
     * 在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where in(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.IN, value, options);
    }

    /**
     * 不在其中
     *
     * @param fieldName 名称 (注意 : 默认为字段名称 , 不是数据库名称)
     * @param value     比较值
     * @param options   配置
     * @return this 方便链式调用
     */
    public Where notIn(String fieldName, Object value, WhereOption... options) {
        return add1(fieldName, WhereType.NOT_IN, value, options);
    }

    /**
     * <p>Getter for the field <code>whereSQL</code>.</p>
     *
     * @return a {@link String} object
     */
    public Object[] whereSQL() {
        return whereSQL;
    }

    /**
     * 设置 whereSQL 适用于 复杂查询的自定义 where 子句<br>
     * 支持三种类型 String , WhereBody 和 AbstractPlaceholderSQL
     * 在最终 cool.scx.sql 中会拼接到 where 子句的最后<br>
     * 注意 :  除特殊语法外不需要手动在头部添加 AND
     *
     * @param whereSQL cool.scx.sql 语句
     * @return 本身 , 方便链式调用
     */
    public Where whereSQL(Object... whereSQL) {
        this.whereSQL = whereSQL;
        return this;
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
    private Where _add(String name, WhereType whereType, Object value1, Object value2, int needParamSize, WhereOption... options) {
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
    public Where remove(String name) {
        whereBodyList.removeIf(w -> w.name().equals(name.trim()));
        return this;
    }

    /**
     * 清除所有 where 条件 (不包括 whereSQL)
     *
     * @return this 方便链式调用
     */
    public Where clear() {
        whereBodyList.clear();
        return this;
    }

    /**
     * 清楚 where 条件中的 whereSQL
     *
     * @return this 方便链式调用
     */
    public Where clearWhereSQL() {
        whereSQL = new Object[0];
        return this;
    }

    /**
     * 清除所有 where 条件 (包括 whereSQL)
     *
     * @return this 方便链式调用
     */
    public Where clearAll() {
        clear();
        clearWhereSQL();
        return this;
    }

    public List<WhereBody> whereBodyList() {
        return whereBodyList;
    }

}
