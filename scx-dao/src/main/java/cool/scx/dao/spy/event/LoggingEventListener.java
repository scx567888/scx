package cool.scx.dao.spy.event;


import cool.scx.dao.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;


public class LoggingEventListener extends SimpleJdbcEventListener {

    private static final Logger logger = LoggerFactory.getLogger("ScxSpy");
    private final Dialect dialect;

    public LoggingEventListener(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public void onBeforeAnyExecute(Statement statement) {
        if (logger.isDebugEnabled()) {
//            logger.debug(getFinalSQL(statement));
        }
    }

    @Override
    public void onAfterAnyExecute(Statement statement, long timeElapsedNanos, SQLException e) {
        if (logger.isDebugEnabled()) {
//            logger.debug("耗时 : " + timeElapsedNanos / 1000_000 + " ms ," + getFinalSQL(statement));
            logger.debug(dialect.getFinalSQL(statement));
        }
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
