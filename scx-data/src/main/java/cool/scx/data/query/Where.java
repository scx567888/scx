package cool.scx.data.query;

import cool.scx.common.util.ArrayUtils;
import cool.scx.data.Query;

import java.util.Arrays;

/**
 * where 查询条件封装类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Where extends QueryLike<Where> {

    /**
     * 自定义的查询语句
     */
    private Object[] clauses;

    /**
     * 创建一个 Where 对象
     */
    public Where() {
        this.clauses = new Object[]{};
    }

    /**
     * 根据旧的 Where 创建一个 Where 对象
     *
     * @param oldWhere 旧的 Where
     */
    public Where(Where oldWhere) {
        this.clauses = Arrays.copyOf(oldWhere.clauses, oldWhere.clauses.length);
    }

    /**
     * 查询条件是否为空
     *
     * @return a boolean
     */
    public boolean isEmpty() {
        return clauses.length == 0;
    }

    /**
     * 设置 whereSQL 适用于 复杂查询的自定义 where 子句<br>
     * 支持三种类型 String , WhereBody 和 AbstractPlaceholderSQL
     * 在最终 cool.scx.sql 中会拼接到 where 子句的最后<br>
     * 注意 :  除特殊语法外不需要手动在头部添加 AND
     *
     * @param whereClauses cool.scx.sql 语句
     * @return 本身 , 方便链式调用
     */
    public Where set(Object... whereClauses) {
        this.clauses = whereClauses;
        return this;
    }

    public Where add(Object... whereClauses) {
        this.clauses = ArrayUtils.concat(this.clauses, whereClauses);
        return this;
    }

    public Object[] clauses() {
        return this.clauses;
    }

    /**
     * 清除所有 where 条件 (不包括 whereSQL)
     *
     * @return this 方便链式调用
     */
    public Where clear() {
        clauses = new Object[]{};
        return this;
    }

    @Override
    public Query toQuery() {
        return new QueryImpl(this);
    }

}
