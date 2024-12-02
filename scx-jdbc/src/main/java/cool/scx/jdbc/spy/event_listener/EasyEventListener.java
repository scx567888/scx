package cool.scx.jdbc.spy.event_listener;

import cool.scx.jdbc.spy.SpyEventListener;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * EasyEventListener
 *
 * @author scx567888
 * @version 0.0.1
 */
public class EasyEventListener implements SpyEventListener {

    public void onBeforeAnyExecute(Statement statement) {

    }

    public void onAfterAnyExecute(Statement statement, long timeElapsedNanos, SQLException e) {

    }

    public void onBeforeAnyAddBatch(Statement statement) {

    }

    public void onAfterAnyAddBatch(Statement statement, long timeElapsedNanos, SQLException e) {

    }

    @Override
    public void onBeforeExecute(PreparedStatement Statement) {
        onBeforeAnyExecute(Statement);
    }

    @Override
    public void onBeforeExecute(Statement Statement, String sql) {
        onBeforeAnyExecute(Statement);
    }

    @Override
    public void onBeforeExecuteBatch(Statement Statement) {
        onBeforeAnyExecute(Statement);
    }

    @Override
    public void onBeforeExecuteUpdate(PreparedStatement Statement) {
        onBeforeAnyExecute(Statement);
    }

    @Override
    public void onBeforeExecuteUpdate(Statement Statement, String sql) {
        onBeforeAnyExecute(Statement);
    }

    @Override
    public void onBeforeExecuteQuery(PreparedStatement Statement) {
        onBeforeAnyExecute(Statement);
    }

    @Override
    public void onBeforeExecuteQuery(Statement Statement, String sql) {
        onBeforeAnyExecute(Statement);
    }

    @Override
    public void onAfterExecute(PreparedStatement Statement, long timeElapsedNanos, SQLException e) {
        onAfterAnyExecute(Statement, timeElapsedNanos, e);
    }

    @Override
    public void onAfterExecute(Statement Statement, long timeElapsedNanos, String sql, SQLException e) {
        onAfterAnyExecute(Statement, timeElapsedNanos, e);
    }

    @Override
    public void onAfterExecuteBatch(Statement Statement, long timeElapsedNanos, long[] updateCounts, SQLException e) {
        onAfterAnyExecute(Statement, timeElapsedNanos, e);
    }

    @Override
    public void onAfterExecuteUpdate(PreparedStatement Statement, long timeElapsedNanos, long rowCount, SQLException e) {
        onAfterAnyExecute(Statement, timeElapsedNanos, e);
    }

    @Override
    public void onAfterExecuteUpdate(Statement Statement, long timeElapsedNanos, String sql, long rowCount, SQLException e) {
        onAfterAnyExecute(Statement, timeElapsedNanos, e);
    }

    @Override
    public void onAfterExecuteQuery(PreparedStatement Statement, long timeElapsedNanos, SQLException e) {
        onAfterAnyExecute(Statement, timeElapsedNanos, e);
    }

    @Override
    public void onAfterExecuteQuery(Statement Statement, long timeElapsedNanos, String sql, SQLException e) {
        onAfterAnyExecute(Statement, timeElapsedNanos, e);
    }

    @Override
    public void onBeforeAddBatch(PreparedStatement Statement) {
        onBeforeAnyAddBatch(Statement);
    }

    @Override
    public void onBeforeAddBatch(Statement Statement, String sql) {
        onBeforeAnyAddBatch(Statement);
    }

    @Override
    public void onAfterAddBatch(PreparedStatement Statement, long timeElapsedNanos, SQLException e) {
        onAfterAnyAddBatch(Statement, timeElapsedNanos, e);
    }

    @Override
    public void onAfterAddBatch(Statement Statement, long timeElapsedNanos, String sql, SQLException e) {
        onAfterAnyAddBatch(Statement, timeElapsedNanos, e);
    }

}
