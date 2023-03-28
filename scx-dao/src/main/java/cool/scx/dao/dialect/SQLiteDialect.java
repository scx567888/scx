package cool.scx.dao.dialect;

import cool.scx.dao.mapping.ColumnInfo;
import cool.scx.dao.mapping.TableInfo;
import org.sqlite.SQLiteDataSource;
import org.sqlite.core.CorePreparedStatement;
import org.sqlite.core.CoreStatement;
import org.sqlite.jdbc4.JDBC4PreparedStatement;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.util.StringUtils.notBlank;
import static cool.scx.util.StringUtils.notEmpty;

public class SQLiteDialect implements Dialect {

    static final Field CoreStatement_sql;
    static final Field CoreStatement_batch;
    static final Field CorePreparedStatement_batchQueryCount;

    static {
        try {
            CoreStatement_sql = CoreStatement.class.getDeclaredField("sql");
            CoreStatement_batch = CoreStatement.class.getDeclaredField("batch");
            CorePreparedStatement_batchQueryCount = CorePreparedStatement.class.getDeclaredField("batchQueryCount");
            CoreStatement_sql.setAccessible(true);
            CoreStatement_batch.setAccessible(true);
            CorePreparedStatement_batchQueryCount.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 当前列对象特殊的 DDL 如设置是否为主键 是否创建索引 是否是唯一值 (建表语句片段 , 需和 normalDDL 一起使用才完整)
     */
    public static String[] initSpecialDDL(ColumnInfo column) {
        if (column == null) {
            return new String[0];
        }
        var name = column.columnName();
        var list = new ArrayList<String>();
//        if (column.unique()) {
//            list.add("UNIQUE KEY `unique_" + name + "`(`" + name + "`)");
//        }
//        if (column.needIndex()) {
//            list.add("KEY `index_" + name + "`(`" + name + "`)");
//        }
        return list.toArray(String[]::new);
    }

    @Override
    public boolean canHandle(DataSource dataSource) {
        try {
            return dataSource instanceof SQLiteDataSource || dataSource.isWrapperFor(SQLiteDataSource.class);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean canHandle(Driver driver) {
        return driver instanceof org.sqlite.JDBC;
    }

    @Override
    public String getFinalSQL(Statement statement) {
        CorePreparedStatement corePreparedStatement;
        try {
            corePreparedStatement = statement.unwrap(JDBC4PreparedStatement.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        final String sql;
        final Object[] batch;
        final int batchQuertCount;
        try {
            sql = (String) CoreStatement_sql.get(corePreparedStatement);
            batch = (Object[]) CoreStatement_batch.get(corePreparedStatement);
            batchQuertCount = (int) CorePreparedStatement_batchQueryCount.get(corePreparedStatement);
        } catch (IllegalAccessException e) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        int currentParameter = 0;
        for (int pos = 0; pos < sql.length(); pos++) {
            char character = sql.charAt(pos);
            if (character == '?' && currentParameter <= batch.length) {
                // 替换 ?
                Object value = batch[currentParameter];
                sb.append(value != null ? value.toString() : "NULL");
                currentParameter = currentParameter + 1;
            } else {
                sb.append(character);
            }
        }
        var finalSQL = sb.toString();
        return batchQuertCount > 1 ? finalSQL + "... 额外的 " + (batchQuertCount - 1) + " 项" : finalSQL;
    }

    @Override
    public String getCreateTableDDL(TableInfo<?> tableInfo) {
        return Dialect.super.getCreateTableDDL(tableInfo);
    }

    @Override
    public List<String> getColumnDefinitions(ColumnInfo[] columnInfos) {
        var columnDefinitions = new ArrayList<String>();
        for (var columnInfo : columnInfos) {
            var normalDDL = getColumnDefinition(columnInfo);
            columnDefinitions.add(normalDDL);
        }
        for (var columnInfo : columnInfos) {
            var specialDDL = initSpecialDDL(columnInfo);
            columnDefinitions.addAll(List.of(specialDDL));
        }
        return columnDefinitions;
    }

    @Override
    public String getDataTypeDefinitionByClass(Class<?> javaType) {
        if (javaType == Integer.class || javaType == Long.class) {
            return "INTEGER";
        } else if (javaType == String.class) {
            return "TEXT";
        } else {
            return "BLOB";
        }
    }

    @Override
    public SQLType getSQLType(Class<?> javaType) {
        return null;
    }

    @Override
    public String getLimitSQL(String sql, Integer rowCount, Integer offset) {
        var limitClauses = rowCount == null ? "" : offset == null || offset == 0 ? " LIMIT " + rowCount : " LIMIT " + offset + "," + rowCount;
        return sql + limitClauses;
    }

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    private String getColumnDefinition(ColumnInfo column) {
        var list = new ArrayList<String>();
        list.add("`" + column.columnName() + "`");
        list.add(getDataTypeDefinition(column));
        list.add(column.notNull() || column.primaryKey() ? "NOT NULL" : "NULL");
        if (column.primaryKey() && column.autoIncrement()) {
            list.add("PRIMARY KEY AUTOINCREMENT");
        }
        if (column.unique()) {
            list.add("UNIQUE ");
        }
        if (notBlank(column.defaultValue())) {
            list.add("DEFAULT " + column.defaultValue());
        }
//        if (notBlank(column.onUpdateValue())) {
//            list.add("ON UPDATE " + column.onUpdateValue());
//        }
        return String.join(" ", list);
    }

    public String getDataTypeDefinition(ColumnInfo column) {
        if (notEmpty(column.type())) {
            return column.type();
        } else {
            return getDataTypeDefinitionByClass(column.javaField().getType());
        }
    }

}
