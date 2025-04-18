package cool.scx.jdbc.sql;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;

import static cool.scx.common.util.StringUtils.notEmpty;

/// 此 SQLBuilder 并不用于构建 [SQL] 只是用于构建普通的 SQL 语句
///
/// @author scx567888
/// @version 0.0.1
public final class SQLBuilder {

    private final SQLBuilderType sqlBuilderType;
    private Object[] selectColumns = null;// 存储列名 如 [name, age]
    private Object table = null;
    private String whereClause = null;// 存储子句 如 [name = 'scx', age > 18]
    private Object[] groupByColumns = null;// 存储列名 如 [name, age]
    private String[] orderByClauses = null;// 存储子句 如 [name desc, age asc]
    private Long offset = null;
    private Long limit = null;
    private Object[] insertColumns = null;// 存储列名 如 [name, age]
    private String[] insertValues = null;// 存储 values 如 ['scx', 1]
    private String[] updateSetClauses = null;//存储子句 如 [name = 'scx', age = 18]

    private SQLBuilder(SQLBuilderType sqlBuilderType) {
        this.sqlBuilderType = sqlBuilderType;
    }

    public static SQLBuilder Select(Object... selectColumns) {
        return new SQLBuilder(SQLBuilderType.SELECT)._Select(selectColumns);
    }

    public static SQLBuilder Insert(Object table, Object... insertColumnInfos) {
        return new SQLBuilder(SQLBuilderType.INSERT)._Insert(table, insertColumnInfos);
    }

    public static SQLBuilder Update(Object table) {
        return new SQLBuilder(SQLBuilderType.UPDATE)._Update(table);
    }

    public static SQLBuilder Delete(Object table) {
        return new SQLBuilder(SQLBuilderType.DELETE)._Delete(table);
    }

    private static String joinWithQuoteIdentifier(Object[] values, Dialect dialect) {
        var isFirst = true;
        var sb = new StringBuilder();
        for (var value : values) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            if (value instanceof Column c) {
                sb.append(dialect.quoteIdentifier(c.name()));
            } else {
                sb.append(value.toString());
            }
        }
        return sb.toString();
    }

    private SQLBuilder _Select(Object[] selectColumns) {
        if (selectColumns.length == 0) {
            throw new IllegalArgumentException("Select 子句错误 : 待查询的数据列 不能为空 !!!");
        }
        this.selectColumns = selectColumns;
        return this;
    }

    public SQLBuilder From(Object table) {
        this.table = table;
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
        this.groupByColumns = groupByColumns;
        return this;
    }

    public SQLBuilder OrderBy(String... orderByClauses) {
        this.orderByClauses = orderByClauses;
        return this;
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

    private SQLBuilder _Insert(Object tableName, Object[] insertColumns) {
        this.table = tableName;
        this.insertColumns = insertColumns;
        return this;
    }

    public SQLBuilder Values(String... insertValues) {
        this.insertValues = insertValues;
        return this;
    }

    private SQLBuilder _Update(Object tableName) {
        this.table = tableName;
        return this;
    }

    public SQLBuilder Set(String... updateSetClauses) {
        if (updateSetClauses.length == 0) {
            throw new IllegalArgumentException("Set 子句错误 : 待更新的数据列 不能为空 !!!");
        }
        this.updateSetClauses = updateSetClauses;
        return this;
    }

    public SQLBuilder _Delete(Object tableName) {
        this.table = tableName;
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
        return "INSERT INTO " + getTableName(dialect) + " (" + getInsertColumns(dialect) + ") VALUES (" + String.join(", ", insertValues) + ")";
    }

    private String GetUpdateSQL(Dialect dialect) {
        var sql = "UPDATE " + getTableName(dialect) + " SET " + String.join(", ", updateSetClauses) + getWhereClause() + getOrderByClause();
        // 更新时 limit 不能有 offset (偏移量)
        return dialect.getLimitSQL(sql, null, limit);
    }

    private String GetDeleteSQL(Dialect dialect) {
        var sql = "DELETE FROM " + getTableName(dialect) + getWhereClause() + getOrderByClause();
        // 删除时 limit 不能有 offset (偏移量)
        return dialect.getLimitSQL(sql, null, limit);
    }

    private String GetSelectSQL(Dialect dialect) {
        var sql = "SELECT " + getSelectColumns(dialect) + " FROM " + getTableName(dialect) + getWhereClause() + getGroupByClause(dialect) + getOrderByClause();
        return dialect.getLimitSQL(sql, offset, limit);
    }

    private String getWhereClause() {
        return notEmpty(whereClause) ? " WHERE " + whereClause : "";
    }

    private String getGroupByClause(Dialect dialect) {
        return groupByColumns != null && groupByColumns.length != 0 ? " GROUP BY " + getGroupByColumns(dialect) : "";
    }

    private String getOrderByClause() {
        return orderByClauses != null && orderByClauses.length != 0 ? " ORDER BY " + String.join(", ", orderByClauses) : "";
    }

    private String getInsertColumns(Dialect dialect) {
        return joinWithQuoteIdentifier(insertColumns, dialect);
    }

    private String getSelectColumns(Dialect dialect) {
        return joinWithQuoteIdentifier(selectColumns, dialect);
    }

    private String getGroupByColumns(Dialect dialect) {
        return joinWithQuoteIdentifier(groupByColumns, dialect);
    }

    private String getTableName(Dialect dialect) {
        if (table instanceof Table t) {
            return dialect.quoteIdentifier(t.name());
        } else {
            return table.toString();
        }
    }

    private enum SQLBuilderType {
        INSERT, DELETE, UPDATE, SELECT
    }

}
