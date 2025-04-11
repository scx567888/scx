package cool.scx.jdbc.sql;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;

import java.util.Arrays;

import static cool.scx.common.util.StringUtils.notEmpty;

/// 此 SQLBuilder 并不用于构建 [SQL] 只是用于构建普通的 SQL 语句
///
/// @author scx567888
/// @version 0.0.1
public final class SQLBuilder {

    private final SQLBuilderType sqlBuilderType;
    private String[] selectColumns = null;// 存储列名 如 [name, age]
    private String tableName = null;
    private String whereClause = null;// 存储子句 如 [name = 'scx', age > 18]
    private String[] groupByColumns = null;// 存储列名 如 [name, age]
    private String[] orderByClauses = null;// 存储子句 如 [name desc, age asc]
    private Long offset = null;
    private Long limit = null;
    private String[] insertColumns = null;// 存储列名 如 [name, age]
    private String[] insertValues = null;// 存储 values 如 ['scx', 1]
    private String[] updateSetClauses = null;//存储子句 如 [name = 'scx', age = 18]

    private SQLBuilder(SQLBuilderType sqlBuilderType) {
        this.sqlBuilderType = sqlBuilderType;
    }

    public static SQLBuilder Select(String... selectColumns) {
        return new SQLBuilder(SQLBuilderType.SELECT)._Select(selectColumns);
    }

    public static SQLBuilder Select(Column... selectColumns) {
        return Select(Arrays.stream(selectColumns).map(Column::name).toArray(String[]::new));
    }

    public static SQLBuilder Insert(String tableName, String... insertColumns) {
        return new SQLBuilder(SQLBuilderType.INSERT)._Insert(tableName, insertColumns);
    }

    public static SQLBuilder Insert(String tableName, Column... insertColumnInfos) {
        return Insert(tableName, Arrays.stream(insertColumnInfos).map(Column::name).toArray(String[]::new));
    }

    public static SQLBuilder Insert(Table table, Column... insertColumnInfos) {
        return Insert(table.name(), Arrays.stream(insertColumnInfos).map(Column::name).toArray(String[]::new));
    }

    public static SQLBuilder Insert(Table table, String... insertColumns) {
        return Insert(table.name(), insertColumns);
    }

    public static SQLBuilder Update(String tableName) {
        return new SQLBuilder(SQLBuilderType.UPDATE)._Update(tableName);
    }

    public static SQLBuilder Update(Table table) {
        return Update(table.name());
    }

    public static SQLBuilder Delete(String tableName) {
        return new SQLBuilder(SQLBuilderType.DELETE)._Delete(tableName);
    }

    public static SQLBuilder Delete(Table table) {
        return Delete(table.name());
    }

    private static String joinWithQuoteIdentifier(String[] values, Dialect dialect) {
        var isFirst = true;
        var sb = new StringBuilder();
        for (var value : values) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            sb.append(dialect.quoteIdentifier(value));
        }
        return sb.toString();
    }

    private SQLBuilder _Select(String... selectColumns) {
        if (selectColumns.length == 0) {
            throw new IllegalArgumentException("Select 子句错误 : 待查询的数据列 不能为空 !!!");
        }
        this.selectColumns = selectColumns;
        return this;
    }

    public SQLBuilder From(Table table) {
        return From(table.name());
    }

    public SQLBuilder From(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SQLBuilder Where(String whereClause) {
        this.whereClause = whereClause;
        return this;
    }

    public SQLBuilder GroupBy(String... groupByColumns) {
        this.groupByColumns = groupByColumns;
        return this;
    }

    public SQLBuilder GroupBy(Column... groupByColumns) {
        return GroupBy(Arrays.stream(groupByColumns).map(Column::name).toArray(String[]::new));
    }

    public SQLBuilder OrderBy(String... orderByClauses) {
        this.orderByClauses = orderByClauses;
        return this;
    }

    public SQLBuilder OrderBy(Column... orderByClauses) {
        return OrderBy(Arrays.stream(orderByClauses).map(Column::name).toArray(String[]::new));
    }

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

    public SQLBuilder Limit(Long size) {
        return Limit(0L, size);
    }

    private SQLBuilder _Insert(String tableName, String... insertColumns) {
        this.tableName = tableName;
        this.insertColumns = insertColumns;
        return this;
    }

    public SQLBuilder Values(String... insertValues) {
        this.insertValues = insertValues;
        return this;
    }

    private SQLBuilder _Update(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SQLBuilder Set(String... updateSetClauses) {
        if (updateSetClauses.length == 0) {
            throw new IllegalArgumentException("Set 子句错误 : 待更新的数据列 不能为空 !!!");
        }
        this.updateSetClauses = updateSetClauses;
        return this;
    }

    public SQLBuilder _Delete(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String GetSQL(Dialect dialect) {
        return switch (this.sqlBuilderType) {
            case INSERT -> GetInsertSQL(dialect);
            case UPDATE -> GetUpdateSQL(dialect);
            case DELETE -> GetDeleteSQL(dialect);
            case SELECT -> GetSelectSQL(dialect);
        };
    }

    private String GetInsertSQL(Dialect dialect) {
        return "INSERT INTO " + dialect.quoteIdentifier(tableName) + " (" + joinWithQuoteIdentifier(insertColumns, dialect) + ") VALUES (" + String.join(", ", insertValues) + ")";
    }

    private String GetUpdateSQL(Dialect dialect) {
        var sql = "UPDATE " + dialect.quoteIdentifier(tableName) + " SET " + String.join(", ", updateSetClauses) + getWhereClause() + getOrderByClause();
        // 更新时 limit 不能有 offset (偏移量)
        return dialect.getLimitSQL(sql, null, limit);
    }

    private String GetDeleteSQL(Dialect dialect) {
        var sql = "DELETE FROM " + dialect.quoteIdentifier(tableName) + getWhereClause() + getOrderByClause();
        // 删除时 limit 不能有 offset (偏移量)
        return dialect.getLimitSQL(sql, null, limit);
    }

    private String GetSelectSQL(Dialect dialect) {
        var sql = "SELECT " + joinWithQuoteIdentifier(selectColumns, dialect) + " FROM " + dialect.quoteIdentifier(tableName) + getWhereClause() + getGroupByClause(dialect) + getOrderByClause();
        return dialect.getLimitSQL(sql, offset, limit);
    }

    private String getWhereClause() {
        return notEmpty(whereClause) ? " WHERE " + whereClause : "";
    }

    private String getGroupByClause(Dialect dialect) {
        return groupByColumns != null && groupByColumns.length != 0 ? " GROUP BY " + joinWithQuoteIdentifier(groupByColumns, dialect) : "";
    }

    private String getOrderByClause() {
        return orderByClauses != null && orderByClauses.length != 0 ? " ORDER BY " + String.join(", ", orderByClauses) : "";
    }

    private enum SQLBuilderType {
        INSERT, DELETE, UPDATE, SELECT
    }

}
