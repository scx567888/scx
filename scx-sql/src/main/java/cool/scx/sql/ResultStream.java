package cool.scx.sql;

import cool.scx.functional.ScxFunction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * ResultSet 迭代器
 *
 * @param <T>
 */
public final class ResultStream<T> implements Iterable<T>, Iterator<T> {

    private final ScxFunction<ResultSet, T, SQLException> converter;
    private final ResultSet resultSet;
    private T next;

    public ResultStream(ScxFunction<ResultSet, T, SQLException> converter, ResultSet resultSet) {
        this.converter = converter;
        this.resultSet = resultSet;
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return temp;
    }

}
