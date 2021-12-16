package cool.scx.sql;

import cool.scx.dao.ScxDaoColumnInfo;
import cool.scx.sql.group_by.GroupBy;
import cool.scx.sql.order_by.OrderBy;
import cool.scx.sql.pagination.Pagination;
import cool.scx.sql.where.Where;

import java.util.Arrays;

/**
 * <p>SQLBuilder class.</p>
 *
 * @author scx567888
 * @version 1.5.0
 */
public final class SQLBuilder {

    /**
     * a
     */
    private final SQLBuilderType sqlBuilderType;

    /**
     * a
     */
    private String[] selectColumns = null;

    /**
     * a
     */
    private String tableName = null;

    /**
     * a
     */
    private String[] whereClauses = null;

    /**
     * a
     */
    private String[] groupByColumns = null;

    /**
     * a
     */
    private String[] orderByClauses = null;

    /**
     * a
     */
    private Integer offset = null;

    /**
     * a
     */
    private Integer size = null;

    /**
     * a
     */
    private String[] insertColumns = null;

    /**
     * a
     */
    private String[] insertValues = null;

    /**
     * a
     */
    private String[] updateSetColumns = null;

    /**
     * 初始化
     *
     * @param sqlBuilderType sql 类型
     */
    private SQLBuilder(SQLBuilderType sqlBuilderType) {
        this.sqlBuilderType = sqlBuilderType;
    }

    /**
     * a
     *
     * @param selectColumns a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public static SQLBuilder Select(String... selectColumns) {
        return new SQLBuilder(SQLBuilderType.SELECT)._Select(selectColumns);
    }

    /**
     * a
     *
     * @return a
     * @ param insertColumnInfos a
     */
    public static SQLBuilder Select(ScxDaoColumnInfo... insertColumnInfos) {
        return Select(Arrays.stream(insertColumnInfos).map(ScxDaoColumnInfo::selectSQL).toArray(String[]::new));
    }

    /**
     * a
     *
     * @param tableName     a {@link java.lang.String} object
     * @param insertColumns a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public static SQLBuilder Insert(String tableName, String... insertColumns) {
        return new SQLBuilder(SQLBuilderType.INSERT)._Insert(tableName, insertColumns);
    }

    /**
     * a
     *
     * @param tableName         a {@link java.lang.String} object
     * @param insertColumnInfos a {@link java.lang.reflect.Field} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public static SQLBuilder Insert(String tableName, ScxDaoColumnInfo... insertColumnInfos) {
        return Insert(tableName, Arrays.stream(insertColumnInfos).map(ScxDaoColumnInfo::name).toArray(String[]::new));
    }

    /**
     * a
     *
     * @param tableName a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public static SQLBuilder Update(String tableName) {
        return new SQLBuilder(SQLBuilderType.UPDATE)._Update(tableName);
    }

    /**
     * a
     *
     * @param tableName a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public static SQLBuilder Delete(String tableName) {
        return new SQLBuilder(SQLBuilderType.DELETE)._Delete(tableName);
    }

    /**
     * a
     *
     * @param selectColumns a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    private SQLBuilder _Select(String... selectColumns) {
        this.selectColumns = selectColumns;
        return this;
    }

    /**
     * a
     *
     * @param tableName a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder From(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * a
     *
     * @param whereClauses a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder Where(String... whereClauses) {
        this.whereClauses = whereClauses;
        return this;
    }

    /**
     * a
     *
     * @param groupByColumns a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder GroupBy(String... groupByColumns) {
        this.groupByColumns = groupByColumns;
        return this;
    }

    /**
     * a
     *
     * @param orderByClauses a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder OrderBy(String... orderByClauses) {
        this.orderByClauses = orderByClauses;
        return this;
    }

    /**
     * a
     *
     * @param offset a int
     * @param size   a int
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder Limit(int offset, int size) {
        this.offset = offset;
        this.size = size;
        return this;
    }

    /**
     * a
     *
     * @param where a {@link Where} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder Where(Where where) {
        if (where != null) {
            this.whereClauses = where.getWhereClauses();
        }
        return this;
    }

    /**
     * a
     *
     * @param groupBy a {@link GroupBy} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder GroupBy(GroupBy groupBy) {
        if (groupBy != null) {
            this.groupByColumns = groupBy.getGroupByColumns();
        }
        return this;
    }

    /**
     * a
     *
     * @param orderBy a {@link OrderBy} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder OrderBy(OrderBy orderBy) {
        if (orderBy != null) {
            this.orderByClauses = orderBy.getOrderByClauses();
        }
        return this;
    }

    /**
     * a
     *
     * @param pagination a {@link Pagination} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder Limit(Pagination pagination) {
        if (pagination != null) {
            this.offset = pagination.offset();
            this.size = pagination.size();
        }
        return this;
    }

    /**
     * a
     *
     * @param tableName     a {@link java.lang.String} object
     * @param insertColumns a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    private SQLBuilder _Insert(String tableName, String... insertColumns) {
        this.tableName = tableName;
        this.insertColumns = insertColumns;
        return this;
    }

    /**
     * a
     *
     * @param insertValues a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder Values(String... insertValues) {
        this.insertValues = insertValues;
        return this;
    }

    /**
     * a
     *
     * @param insertColumnInfos a {@link java.lang.reflect.Field} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder Values(ScxDaoColumnInfo... insertColumnInfos) {
        return Values(Arrays.stream(insertColumnInfos).map(ScxDaoColumnInfo::insertValuesSQL).toArray(String[]::new));
    }

    /**
     * a
     *
     * @param tableName a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    private SQLBuilder _Update(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * a
     *
     * @param updateSetColumns a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder Set(String... updateSetColumns) {
        this.updateSetColumns = updateSetColumns;
        return this;
    }

    /**
     * a
     *
     * @param updateSetColumnInfos a {@link java.lang.reflect.Field} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder Set(ScxDaoColumnInfo... updateSetColumnInfos) {
        return Set(Arrays.stream(updateSetColumnInfos).map(ScxDaoColumnInfo::updateSetSQL).toArray(String[]::new));
    }

    /**
     * a
     *
     * @param tableName a {@link java.lang.String} object
     * @return a {@link cool.scx.sql.SQLBuilder} object
     */
    public SQLBuilder _Delete(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * 获取 sql
     *
     * @return a {@link java.lang.String} object.
     */
    public String GetSQL() {
        return switch (this.sqlBuilderType) {
            case INSERT -> GetInsertSQL();
            case UPDATE -> GetUpdateSQL();
            case DELETE -> GetDeleteSQL();
            case SELECT -> GetSelectSQL();
        };
    }

    /**
     * a
     *
     * @return a {@link java.lang.String} object
     */
    private String GetInsertSQL() {
        return "INSERT INTO " + tableName + " (" + String.join(", ", insertColumns) + ") VALUES (" + String.join(", ", insertValues) + ")";
    }

    /**
     * a
     *
     * @return a {@link java.lang.String} object
     */
    private String GetUpdateSQL() {
        if (updateSetColumns.length > 0) {
            return "UPDATE " + tableName + " SET " + String.join(", ", updateSetColumns) + getWhereSQL();
        } else {
            return null;
        }
    }

    /**
     * a
     *
     * @return a {@link java.lang.String} object
     */
    private String GetDeleteSQL() {
        return "DELETE FROM " + tableName + getWhereSQL();
    }

    /**
     * 获取 select SQL
     *
     * @return s
     */
    private String GetSelectSQL() {
        var groupBySQL = groupByColumns != null && groupByColumns.length != 0 ? " GROUP BY " + String.join(", ", groupByColumns) : "";
        var orderBySQL = orderByClauses != null && orderByClauses.length != 0 ? " ORDER BY " + String.join(", ", orderByClauses) : "";
        var limitSQL = offset != null && size != null ? " LIMIT " + offset + "," + size : "";
        return "SELECT " + String.join(", ", selectColumns) + " FROM " + tableName + getWhereSQL() + groupBySQL + orderBySQL + limitSQL;
    }

    /**
     * 获取 where 语句
     *
     * @return w
     */
    private String getWhereSQL() {
        return whereClauses != null && whereClauses.length > 0 ? " WHERE " + String.join(" AND ", whereClauses) : "";
    }

}
