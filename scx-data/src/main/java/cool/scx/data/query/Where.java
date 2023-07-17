package cool.scx.data.query;

import java.util.Arrays;

/**
 * where 查询条件封装类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Where {

    /**
     * 自定义的查询语句
     */
    private Object[] whereClause;

    /**
     * 创建一个 Where 对象
     */
    public Where() {
        this.whereClause = new Object[]{};
    }

    /**
     * 根据旧的 Where 创建一个 Where 对象
     *
     * @param oldWhere 旧的 Where
     */
    public Where(Where oldWhere) {
        this.whereClause = Arrays.copyOf(oldWhere.whereClause, oldWhere.whereClause.length);
    }

    /**
     * 查询条件是否为空
     *
     * @return a boolean
     */
    public boolean isEmpty() {
        return whereClause.length == 0;
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
        this.whereClause = whereClauses;
        return this;
    }

    public Object[] clauses() {
        return this.whereClause;
    }

    /**
     * 清除所有 where 条件 (不包括 whereSQL)
     *
     * @return this 方便链式调用
     */
    public Where clear() {
        whereClause = new Object[]{};
        return this;
    }

}
