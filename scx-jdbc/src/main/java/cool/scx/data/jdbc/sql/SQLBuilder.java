package cool.scx.data.jdbc.sql;

import cool.scx.data.jdbc.dialect.Dialect;
import cool.scx.data.jdbc.mapping.Column;

import java.util.Arrays;

import static cool.scx.util.StringUtils.notEmpty;

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
    private Long offset = null;

    /**
     * a
     */
    private Long limit = null;

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
    public static SQLBuilder Insert(String tableName, Column... insertColumnInfos) {
        return Insert(tableName, Arrays.stream(insertColumnInfos).map(Column::name).toArray(String[]::new));
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
     * @param offset a int
     * @param limit  a int
     * @return a {@link SQLBuilder} object
     */
    public SQLBuilder Limit(Long offset, Long limit) {
        if (offset != null && offset < 0) {
            throw new IllegalArgumentException("分页参数错误 : offset (偏移量) 不能小于 0 !!!");
        }
        if (limit != null && limit < 0) {
            throw new IllegalArgumentException("分页参数错误 : limit (每页数量) 不能小于 0 !!!");
        }
        this.offset = offset;
        this.limit = limit;
        return this;
    }

    /**
     * a
     *
     * @param size a
     * @return a
     */
    public SQLBuilder Limit(Long size) {
        return Limit(0L, size);
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
    public String GetSQL(Dialect dialect) {
        return switch (this.sqlBuilderType) {
            case INSERT -> GetInsertSQL();
            case UPDATE -> GetUpdateSQL(dialect);
            case DELETE -> GetDeleteSQL(dialect);
            case SELECT -> GetSelectSQL(dialect);
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
     * 更新时 limit 不能有 offset (偏移量)
     *
     * @return a {@link String} object
     */
    private String GetUpdateSQL(Dialect dialect) {
        var sql = "UPDATE " + tableName + " SET " + String.join(", ", updateSetColumns) + getWhereClause() + getOrderByClause();
        return dialect.getLimitSQL(sql, null, limit);
    }

    /**
     * 删除时 limit 不能有 offset (偏移量)
     *
     * @return a {@link String} object
     */
    private String GetDeleteSQL(Dialect dialect) {
        var sql = "DELETE FROM " + tableName + getWhereClause() + getOrderByClause();
        return dialect.getLimitSQL(sql, null, limit);
    }

    /**
     * 获取 select SQL
     *
     * @return s
     */
    private String GetSelectSQL(Dialect dialect) {
        var sql = "SELECT " + String.join(", ", selectColumns) + " FROM " + tableName + getWhereClause() + getGroupByClause() + getOrderByClause();
        return dialect.getLimitSQL(sql, offset, limit);
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
