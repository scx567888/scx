package cool.scx.jdbc.dialect._default;

import cool.scx.jdbc.JDBCType;
import cool.scx.jdbc.dialect.DDLBuilder;
import cool.scx.jdbc.dialect.Dialect;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.Statement;

import static cool.scx.jdbc.dialect._default.DefaultDDLBuilder.DEFAULT_DDL_BUILDER;

public final class DefaultDialect extends Dialect {

    public static final Dialect DEFAULT_DIALECT = new DefaultDialect();

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
    public DDLBuilder ddlBuilder() {
        return DEFAULT_DDL_BUILDER;
    }

    @Override
    public DataSource createDataSource(String url, String username, String password, String[] parameters) {
        return null;
    }

    @Override
    public JDBCType dialectDataTypeToJDBCType(String dialectDataType) {
        return null;
    }

    @Override
    public String jdbcTypeToDialectDataType(JDBCType jdbcType) {
        return null;
    }

}
