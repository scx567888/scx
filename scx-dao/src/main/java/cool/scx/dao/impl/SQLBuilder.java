package cool.scx.dao.impl;

import cool.scx.dao.dialect.Dialect;
import cool.scx.sql.mapping.Column;
import cool.scx.sql.sql.SQL;

import java.util.Arrays;

import static cool.scx.util.StringUtils.notEmpty;

/**
 * 此 SQLBuilder 并不用于构建 {@link SQL} 只是用于构建普通的 SQL 语句
 *
 * @author scx567888
 * @version 0.0.1
 */
final class SQLBuilder {

    /**
     * a
     */
    private final SQLBuilderType sqlBuilderType;

    private final Dialect dialect;

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
    private String whereClause = null;

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
    private SQLBuilder(Dialect dialect, SQLBuilderType sqlBuilderType) {
        this.dialect = dialect;
        this.sqlBuilderType = sqlBuilderType;
    }

    /**
     * a
     *
     * @param selectColumns a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Select(Dialect dialect, String... selectColumns) {
        return new SQLBuilder(dialect, SQLBuilderType.SELECT)._Select(selectColumns);
    }

    /**
     * a
     *
     * @param tableName     a {@link String} object
     * @param insertColumns a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Insert(Dialect dialect, String tableName, String... insertColumns) {
        return new SQLBuilder(dialect, SQLBuilderType.INSERT)._Insert(tableName, insertColumns);
    }

    /**
     * a
     *
     * @param tableName         a {@link String} object
     * @param insertColumnInfos a {@link java.lang.reflect.Field} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Insert(Dialect dialect, String tableName, Column... insertColumnInfos) {
        return Insert(dialect, tableName, Arrays.stream(insertColumnInfos).map(Column::name).toArray(String[]::new));
    }

    /**
     * a
     *
     * @param tableName a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Update(Dialect dialect, String tableName) {
        return new SQLBuilder(dialect, SQLBuilderType.UPDATE)._Update(tableName);
    }

    /**
     * a
     *
     * @param tableName a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public static SQLBuilder Delete(Dialect dialect, String tableName) {
        return new SQLBuilder(dialect, SQLBuilderType.DELETE)._Delete(tableName);
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
     * @param whereClause a {@link String} object
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder Where(String whereClause) {
        this.whereClause = whereClause;
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
        return "UPDATE " + tableName + " SET " + String.join(", ", updateSetColumns) + getWhereClause();
    }

    /**
     * a
     *
     * @return a {@link String} object
     */
    private String GetDeleteSQL() {
        return "DELETE FROM " + tableName + getWhereClause();
    }

    /**
     * 获取 select SQL
     *
     * @return s
     */
    private String GetSelectSQL() {
        var sql = "SELECT " + String.join(", ", selectColumns) + " FROM " + tableName + getWhereClause() + getGroupByClause() + getOrderByClause();
        return dialect.getLimitSQL(sql, offset, rowCount);
    }

    /**
     * 获取 where 语句
     *
     * @return w
     */
    private String getWhereClause() {
        return notEmpty(whereClause) ? " WHERE " + whereClause : "";
    }

    private String getGroupByClause() {
        return groupByColumns != null && groupByColumns.length != 0 ? " GROUP BY " + String.join(", ", groupByColumns) : "";
    }

    private String getOrderByClause() {
        return orderByClauses != null && orderByClauses.length != 0 ? " ORDER BY " + String.join(", ", orderByClauses) : "";
    }

    enum SQLBuilderType {
        INSERT, DELETE, UPDATE, SELECT
    }

}
