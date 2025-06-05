package cool.scx.jdbc.mysql;

import com.mysql.cj.PreparedQuery;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.NonRegisteringDriver;
import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.mapping.Column;
import cool.scx.jdbc.mapping.Table;
import cool.scx.jdbc.type_handler.TypeHandler;
import cool.scx.jdbc.type_handler.TypeHandlerSelector;

import javax.sql.DataSource;
import java.lang.reflect.Type;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.common.util.StringUtils.notBlank;

/// MySQLDialect
///
/// @author scx567888
/// @version 0.0.1
/// @see <a href="https://dev.mysql.com/doc/refman/8.0/en/create-table.html">https://dev.mysql.com/doc/refman/8.0/en/create-table.html</a>
public class MySQLDialect implements Dialect {

    private static final com.mysql.cj.jdbc.NonRegisteringDriver DRIVER = initDRIVER();
    private final TypeHandlerSelector typeHandlerSelector;

    public MySQLDialect() {
        this.typeHandlerSelector = new TypeHandlerSelector();
    }

    private static NonRegisteringDriver initDRIVER() {
        try {
            return new com.mysql.cj.jdbc.NonRegisteringDriver();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canHandle(String url) {
        try {
            return DRIVER.acceptsURL(url);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean canHandle(DataSource dataSource) {
        try {
            return dataSource instanceof MysqlDataSource || dataSource.isWrapperFor(MysqlDataSource.class);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean canHandle(Driver driver) {
        return driver instanceof com.mysql.cj.jdbc.Driver;
    }

    @Override
    public String getFinalSQL(Statement preparedStatement) {
        ClientPreparedStatement clientPreparedStatement;
        try {
            clientPreparedStatement = preparedStatement.unwrap(ClientPreparedStatement.class);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        var preparedQuery = ((PreparedQuery) clientPreparedStatement.getQuery());
        var finalSQL = preparedQuery.asSql();
        var batchedArgsSize = preparedQuery.getBatchedArgs() == null ? 0 : preparedQuery.getBatchedArgs().size();
        return batchedArgsSize > 1 ? finalSQL + "... 额外的 " + (batchedArgsSize - 1) + " 项" : finalSQL;
    }

    @Override
    public DataSource createDataSource(String url, String username, String password, String[] parameters) {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(url);
        mysqlDataSource.setUser(username);
        mysqlDataSource.setPassword(password);
        // 设置参数值
        for (var parameter : parameters) {
            var p = parameter.split("=");
            if (p.length == 2) {
                var property = mysqlDataSource.getProperty(p[0]);
                property.setValue(property.getPropertyDefinition().parseObject(p[1], null));
            }
        }
        return mysqlDataSource;
    }

    @Override
    public PreparedStatement beforeExecuteQuery(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setFetchSize(Integer.MIN_VALUE);
        return preparedStatement;
    }

    @Override
    public <T> TypeHandler<T> findTypeHandler(Type type) {
        return typeHandlerSelector.findTypeHandler(type);
    }

    @Override
    public JDBCType dialectDataTypeToJDBCType(String dialectDataType) {
        return MySQLDialectHelper.dialectDataTypeToJDBCType(dialectDataType);
    }

    @Override
    public String jdbcTypeToDialectDataType(JDBCType jdbcType) {
        return MySQLDialectHelper.jdbcTypeToDialectDataType(jdbcType).getName();
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return "`" + identifier + "`";
    }

    @Override
    public List<String> getColumnConstraint(Column column) {
        var list = new ArrayList<String>();
        list.add(column.notNull() || column.primary() ? "NOT NULL" : "NULL");
        if (column.autoIncrement()) {
            list.add("AUTO_INCREMENT");
        }
        if (notBlank(column.defaultValue())) {
            list.add("DEFAULT " + column.defaultValue());
        }
        if (notBlank(column.onUpdate())) {
            list.add("ON UPDATE " + column.onUpdate());
        }
        return list;
    }

    @Override
    public String getDataTypeNameByJDBCType(JDBCType dataType) {
        var mysqlType = MySQLDialectHelper.jdbcTypeToDialectDataType(dataType);
        return mysqlType.getName();
    }

    @Override
    public List<String> getTableConstraint(Table table) {
        var list = new ArrayList<String>();
        for (var column : table.columns()) {
            var name = column.name();
            if (column.primary()) {
                list.add("PRIMARY KEY (" + quoteIdentifier(name) + ")");
            }
            if (column.unique()) {
                var key = "unique_" + name;
                list.add("UNIQUE KEY " + quoteIdentifier(key) + "(" + quoteIdentifier(name) + ")");
            }
            if (column.index()) {
                var key = "index_" + name;
                list.add("KEY " + quoteIdentifier(key) + "(" + quoteIdentifier(name) + ")");
            }
        }
        return list;
    }

    @Override
    public String defaultDataType() {
        return "VARCHAR(128)";
    }

}
