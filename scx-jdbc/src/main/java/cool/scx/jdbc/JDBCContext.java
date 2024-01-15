package cool.scx.jdbc;

import cool.scx.jdbc.dialect.Dialect;
import cool.scx.jdbc.sql.SQLRunner;

import javax.sql.DataSource;

import static cool.scx.jdbc.dialect.DialectSelector.findDialect;

public class JDBCContext {

    private final DataSource dataSource;
    private final Dialect dialect;
    private final SQLRunner sqlRunner;

    public JDBCContext(DataSource dataSource) {
        this.dataSource = dataSource;
        this.dialect = findDialect(dataSource);
        this.sqlRunner = new SQLRunner(this);
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

}
