package cool.scx.sql.spy.event;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;

import static cool.scx.sql.JDBCHelperRegistry.getSQL;


public class LoggingEventListener extends SimpleJdbcEventListener {

    public static final LoggingEventListener INSTANCE = new LoggingEventListener();
    private static final Logger logger = LoggerFactory.getLogger(LoggingEventListener.class);

    @Override
    public void onBeforeAnyExecute(Statement statement) {
        logger.debug(getSQL(statement));
    }

    @Override
    public void onAfterAnyExecute(Statement statement, long timeElapsedNanos, SQLException e) {
        logger.debug("耗时 [{}] : " + getSQL(statement), timeElapsedNanos, e);
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
