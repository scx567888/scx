package cool.scx.dao.dialect;

import cool.scx.sql.mapping.Column;
import org.sqlite.SQLiteDataSource;
import org.sqlite.core.CorePreparedStatement;
import org.sqlite.core.CoreStatement;
import org.sqlite.jdbc4.JDBC4PreparedStatement;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.util.StringUtils.notBlank;

/**
 * @see <a href="https://www.sqlite.org/lang_createtable.html">https://www.sqlite.org/lang_createtable.html</a>
 */
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
    public String getLimitSQL(String sql, Integer offset, Integer rowCount) {
        var limitClauses = rowCount == null ? "" : offset == null || offset == 0 ? " LIMIT " + rowCount : " LIMIT " + offset + "," + rowCount;
        return sql + limitClauses;
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

    /**
     * 当前列对象通常的 DDL 如设置 字段名 类型 是否可以为空 默认值等 (建表语句片段 , 需和 specialDDL 一起使用才完整)
     */
    @Override
    public List<String> getColumnConstraint(Column column) {
        var list = new ArrayList<String>();
        if (column.primaryKey() && column.autoIncrement()) {
            list.add("PRIMARY KEY AUTOINCREMENT");
        }
        list.add(column.notNull() || column.primaryKey() ? "NOT NULL" : "NULL");
        if (column.unique()) {
            list.add("UNIQUE");
        }
        if (notBlank(column.defaultValue())) {
            list.add("DEFAULT " + column.defaultValue());
        }
        return list;
    }

}
