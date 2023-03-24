package cool.scx.dao;

import cool.scx.sql.SQL;
import cool.scx.dao.mapping.ColumnInfo;

import java.util.Arrays;

/**
 * 此 SQLBuilder 并不用于构建 {@link SQL} 只是用于构建普通的 SQL 语句
 *
 * @author scx567888
 * @version 0.0.1
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
    private Integer rowCount = null;

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
     * @param sqlBuilderType cool.scx.sql 类型
     */
    private SQLBuilder(SQLBuilderType sqlBuilderType) {
        this.sqlBuilderType = sqlBuilderType;
    }

    /**
     * a
     *
     * @param selectColumns a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Select(String... selectColumns) {
        return new SQLBuilder(SQLBuilderType.SELECT)._Select(selectColumns);
    }

    /**
     * a
     *
     * @param tableName     a {@link String} object
     * @param insertColumns a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Insert(String tableName, String... insertColumns) {
        return new SQLBuilder(SQLBuilderType.INSERT)._Insert(tableName, insertColumns);
    }

    /**
     * a
     *
     * @param tableName         a {@link String} object
     * @param insertColumnInfos a {@link java.lang.reflect.Field} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Insert(String tableName, ColumnInfo... insertColumnInfos) {
        return Insert(tableName, Arrays.stream(insertColumnInfos).map(ColumnInfo::columnName).toArray(String[]::new));
    }

    /**
     * a
     *
     * @param tableName a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Update(String tableName) {
        return new SQLBuilder(SQLBuilderType.UPDATE)._Update(tableName);
    }

    /**
     * a
     *
     * @param tableName a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Delete(String tableName) {
        return new SQLBuilder(SQLBuilderType.DELETE)._Delete(tableName);
    }

    /**
     * a
     *
     * @param selectColumns a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    private SQLBuilder _Select(String... selectColumns) {
        if (selectColumns.length == 0) {
            throw new IllegalArgumentException("Select 子句错误 : 待查询的数据列 不能为空 !!!");
        }
        this.selectColumns = selectColumns;
        return this;
    }

    /**
     * a
     *
     * @param tableName a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder From(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * a
     *
     * @param whereClauses a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder Where(String... whereClauses) {
        this.whereClauses = whereClauses;
        return this;
    }

    /**
     * a
     *
     * @param groupByColumns a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder GroupBy(String... groupByColumns) {
        this.groupByColumns = groupByColumns;
        return this;
    }

    /**
     * a
     *
     * @param orderByClauses a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder OrderBy(String... orderByClauses) {
        this.orderByClauses = orderByClauses;
        return this;
    }

    /**
     * a
     *
     * @param offset   a int
     * @param rowCount a int
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder Limit(Integer offset, Integer rowCount) {
        if (offset != null && offset < 0) {
            throw new IllegalArgumentException("分页参数错误 : offset (偏移量) 不能小于 0 !!!");
        }
        if (rowCount != null && rowCount < 0) {
            throw new IllegalArgumentException("分页参数错误 : rowCount (每页数量) 不能小于 0 !!!");
        }
        this.offset = offset;
        this.rowCount = rowCount;
        return this;
    }

    /**
     * a
     *
     * @param size a
     * @return a
     */
    public SQLBuilder Limit(Integer size) {
        return Limit(0, size);
    }

    /**
     * a
     *
     * @param tableName     a {@link String} object
     * @param insertColumns a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    private SQLBuilder _Insert(String tableName, String... insertColumns) {
        this.tableName = tableName;
        this.insertColumns = insertColumns;
        return this;
    }

    /**
     * a
     *
     * @param insertValues a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder Values(String... insertValues) {
        this.insertValues = insertValues;
        return this;
    }

    /**
     * a
     *
     * @param tableName a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    private SQLBuilder _Update(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * a
     *
     * @param updateSetColumns a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder Set(String... updateSetColumns) {
        if (updateSetColumns.length == 0) {
            throw new IllegalArgumentException("Set 子句错误 : 待更新的数据列 不能为空 !!!");
        }
        this.updateSetColumns = updateSetColumns;
        return this;
    }

    /**
     * a
     *
     * @param tableName a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder _Delete(String tableName) {
        this.tableName = tableName;
        return this;
    }

    /**
     * 获取 cool.scx.sql
     *
     * @return a {@link String} object.
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
     * @return a {@link String} object
     */
    private String GetInsertSQL() {
        return "INSERT INTO " + tableName + " (" + String.join(", ", insertColumns) + ") VALUES (" + String.join(", ", insertValues) + ")";
    }

    /**
     * a
     *
     * @return a {@link String} object
     */
    private String GetUpdateSQL() {
        return "UPDATE " + tableName + " SET " + String.join(", ", updateSetColumns) + getWhereSQL();
    }

    /**
     * a
     *
     * @return a {@link String} object
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
        return "SELECT " + String.join(", ", selectColumns) + " FROM " + tableName + getWhereSQL() + getGroupBySQL() + getOrderBySQL() + getLimitSQL();
    }

    /**
     * 获取 where 语句
     *
     * @return w
     */
    private String getWhereSQL() {
        return whereClauses != null && whereClauses.length > 0 ? " WHERE " + String.join(" AND ", whereClauses) : "";
    }

    private String getLimitSQL() {
        return rowCount == null ? "" : offset == null || offset == 0 ? " LIMIT " + rowCount : " LIMIT " + offset + "," + rowCount;
    }

    private String getGroupBySQL() {
        return groupByColumns != null && groupByColumns.length != 0 ? " GROUP BY " + String.join(", ", groupByColumns) : "";
    }

    private String getOrderBySQL() {
        return orderByClauses != null && orderByClauses.length != 0 ? " ORDER BY " + String.join(", ", orderByClauses) : "";
    }

    enum SQLBuilderType {
        INSERT, DELETE, UPDATE, SELECT
    }

}
