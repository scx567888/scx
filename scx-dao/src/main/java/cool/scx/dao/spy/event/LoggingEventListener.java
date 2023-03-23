package cool.scx.dao.spy.event;


import com.mysql.cj.PreparedQuery;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;


public class LoggingEventListener extends SimpleJdbcEventListener {

    public static final LoggingEventListener INSTANCE = new LoggingEventListener();
    private static final Logger logger = LoggerFactory.getLogger("ScxSpy");

    /**
     * todo 这里需要支持不同的数据库
     * 　获取最终的 SQL
     *
     * @param preparedStatement a
     * @return a
     */
    public static String getFinalSQL(Statement preparedStatement) {
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
    public void onBeforeAnyExecute(Statement statement) {
        if (logger.isDebugEnabled()) {
//            logger.debug(getFinalSQL(statement));
        }
    }

    @Override
    public void onAfterAnyExecute(Statement statement, long timeElapsedNanos, SQLException e) {
        if (logger.isDebugEnabled()) {
//            logger.debug("耗时 : " + timeElapsedNanos / 1000_000 + " ms ," + getFinalSQL(statement));
            logger.debug(getFinalSQL(statement));
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
