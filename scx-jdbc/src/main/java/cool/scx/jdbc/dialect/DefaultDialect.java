package cool.scx.jdbc.dialect;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.type_handler.TypeHandler;
import cool.scx.jdbc.type_handler.TypeHandlerSelector;
import cool.scx.reflect.TypeInfo;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.Statement;

/// DefaultDialect
///
/// @author scx567888
/// @version 0.0.1
public final class DefaultDialect implements Dialect {

    public static final Dialect DEFAULT_DIALECT = new DefaultDialect();

    private final TypeHandlerSelector typeHandlerSelector = new TypeHandlerSelector();

    private DefaultDialect() {

    }

    @Override
    public boolean canHandle(String url) {
        return false;
    }

    @Override
    public boolean canHandle(DataSource dataSource) {
        return false;
    }

    @Override
    public boolean canHandle(Driver driver) {
        return false;
    }

    @Override
    public String getFinalSQL(Statement statement) {
        return null;
    }

    @Override
    public DataSource createDataSource(String url, String username, String password, String[] parameters) {
        return null;
    }

    @Override
    public <T> TypeHandler<T> findTypeHandler(Class<?> type) {
        return typeHandlerSelector.findTypeHandler(type);
    }

    @Override
    public <T> TypeHandler<T> findTypeHandler(TypeInfo type) {
        return typeHandlerSelector.findTypeHandler(type);
    }

    @Override
    public JDBCType dialectDataTypeToJDBCType(String dialectDataType) {
        return null;
    }

    @Override
    public String jdbcTypeToDialectDataType(JDBCType jdbcType) {
        return null;
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return identifier;
    }

    @Override
    public String getDataTypeNameByJDBCType(JDBCType dataType) {
        return dataType.name();
    }

}
