package cool.scx.dao.dialect;

import cool.scx.dao.mapping.ColumnInfo;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.SQLType;
import java.sql.Statement;
import java.util.List;

public class SQLServerDialect implements Dialect {

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
    public List<String> getColumnDefinitions(ColumnInfo[] tableInfo) {
        return null;
    }

    @Override
    public String getDataTypeDefinitionByClass(Class<?> javaType) {
        return null;
    }

    @Override
    public SQLType getSQLType(Class<?> javaType) {
        return null;
    }

    @Override
    public String getLimitSQL(String sql, Integer rowCount, Integer offset) {
        return null;
    }
}
