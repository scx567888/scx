package cool.scx.data.jdbc.dialect;

import cool.scx.data.jdbc.mapping.Column;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.Statement;
import java.util.List;

public final class StandardDialect extends Dialect {

    public static final Dialect STANDARD_DIALECT = new StandardDialect();

    private StandardDialect() {

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
    public String getLimitSQL(String sql, Long offset, Long rowCount) {
        return null;
    }

    @Override
    public DataSource createDataSource(String url, String username, String password, String[] p) {
        return null;
    }

    @Override
    public List<String> getColumnConstraint(Column columns) {
        return null;
    }

    @Override
    public String getDataTypeDefinitionByClass(Class<?> javaType) {
        return null;
    }

}
