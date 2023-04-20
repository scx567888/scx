package cool.scx.data.jdbc;

import cool.scx.data.jdbc.dialect.Dialect;
import cool.scx.data.jdbc.sql.SQLRunner;
import cool.scx.data.jdbc.type_handler.TypeHandlerSelector;

import javax.sql.DataSource;

import static cool.scx.data.jdbc.dialect.DialectSelector.findDialect;

public class JDBCContext {

    private final DataSource dataSource;
    private final Dialect dialect;
    private final SQLRunner sqlRunner;
    private final TypeHandlerSelector typeHandlerSelector;

    public JDBCContext(DataSource dataSource) {
        this.dataSource = dataSource;
        this.dialect = findDialect(dataSource);
        this.sqlRunner = new SQLRunner(this);
        this.typeHandlerSelector = new TypeHandlerSelector(this.dialect);
    }

    public SQLRunner sqlRunner() {
        return sqlRunner;
    }

    public Dialect dialect() {
        return this.dialect;
    }

    public DataSource dataSource() {
        return this.dataSource;
    }

    public TypeHandlerSelector typeHandlerSelector() {
        return typeHandlerSelector;
    }

}
