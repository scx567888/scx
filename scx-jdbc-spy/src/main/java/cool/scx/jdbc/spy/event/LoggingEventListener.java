package cool.scx.jdbc.spy.event;

import cool.scx.jdbc.dialect.Dialect;

import java.lang.System.Logger;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.System.Logger.Level.DEBUG;

public class LoggingEventListener extends SimpleJdbcEventListener {

    private static final Logger logger = System.getLogger("ScxSpy");
    private final Dialect dialect;

    public LoggingEventListener(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public void onBeforeAnyExecute(Statement statement) {
        if (logger.isLoggable(DEBUG)) {
            logger.log(DEBUG, dialect.getFinalSQL(statement));
        }
    }

    @Override
    public void onAfterAnyExecute(Statement statement, long timeElapsedNanos, SQLException e) {
//        todo 需要处理(是否需要在 语句执行后执行此方法)
//        if (logger.isDebugEnabled()) {
//            logger.debug("耗时 : " + timeElapsedNanos / 1000_000 + " ms ," + getFinalSQL(statement));
//            logger.debug(dialect.getFinalSQL(statement));
//        }
    }

    @Override
    public void onBeforeAnyAddBatch(Statement Statement) {
        super.onBeforeAnyAddBatch(Statement);
    }

    @Override
    public void onAfterAnyAddBatch(Statement Statement, long timeElapsedNanos, SQLException e) {
        super.onAfterAnyAddBatch(Statement, timeElapsedNanos, e);
    }

}
