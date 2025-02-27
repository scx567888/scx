package cool.scx.jdbc.spy;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/// SpyEventListener
///
/// @author scx567888
/// @version 0.0.1
public interface SpyEventListener {

    default void onBeforeAddBatch(PreparedStatement statementInformation) {

    }

    default void onAfterAddBatch(PreparedStatement statementInformation, long timeElapsedNanos, SQLException e) {

    }

    default void onBeforeAddBatch(Statement statementInformation, String sql) {

    }

    default void onAfterAddBatch(Statement statementInformation, long timeElapsedNanos, String sql, SQLException e) {

    }

    default void onBeforeExecute(PreparedStatement statementInformation) {

    }

    default void onAfterExecute(PreparedStatement statementInformation, long timeElapsedNanos, SQLException e) {

    }

    default void onBeforeExecute(Statement statementInformation, String sql) {

    }

    default void onAfterExecute(Statement statementInformation, long timeElapsedNanos, String sql, SQLException e) {

    }

    default void onBeforeExecuteBatch(Statement statementInformation) {

    }

    default void onAfterExecuteBatch(Statement statementInformation, long timeElapsedNanos, long[] updateCounts, SQLException e) {

    }

    default void onBeforeExecuteUpdate(PreparedStatement statementInformation) {

    }

    default void onAfterExecuteUpdate(PreparedStatement statementInformation, long timeElapsedNanos, long rowCount, SQLException e) {

    }

    default void onBeforeExecuteUpdate(Statement statementInformation, String sql) {

    }

    default void onAfterExecuteUpdate(Statement statementInformation, long timeElapsedNanos, String sql, long rowCount, SQLException e) {

    }

    default void onBeforeExecuteQuery(PreparedStatement statementInformation) {

    }

    default void onAfterExecuteQuery(PreparedStatement statementInformation, long timeElapsedNanos, SQLException e) {

    }

    default void onBeforeExecuteQuery(Statement statementInformation, String sql) {

    }

    default void onAfterExecuteQuery(Statement statementInformation, long timeElapsedNanos, String sql, SQLException e) {

    }

    default void onAfterGetResultSet(Statement statementInformation, long timeElapsedNanos, SQLException e) {

    }

    default void onAfterStatementClose(Statement statementInformation, SQLException e) {

    }

}
