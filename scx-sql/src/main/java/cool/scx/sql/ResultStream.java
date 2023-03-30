package cool.scx.sql;

import cool.scx.functional.ScxFunction;
import cool.scx.functional.ScxRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * ResultSet 迭代器
 *
 * @param <T>
 */
public final class ResultStream<T> implements Iterable<T>, Iterator<T>, AutoCloseable {

    private final ScxFunction<ResultSet, T, SQLException> converter;
    private final ResultSet resultSet;
    private ScxRunnable<SQLException> onClose;
    private boolean isClosed;
    private T next;

    public ResultStream(ScxFunction<ResultSet, T, SQLException> converter, ResultSet resultSet) {
        this.converter = converter;
        this.resultSet = resultSet;
        this.isClosed = false;
        this.next();
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public T next() {
        var temp = next;
        try {
            if (resultSet.next()) {
                next = converter.apply(resultSet);
            } else {
                next = null;
                //读取不到了 这里调用 close
                close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return temp;
    }

    @Override
    public void close() throws SQLException {
        if (isClosed) {
            return;
        }
        if (onClose != null) {
            onClose.run();
        }
        isClosed = true;
    }

    public ResultStream<T> onClose(ScxRunnable<SQLException> onClose) {
        this.onClose = onClose;
        return this;
    }

}
