package cool.scx.sql.handler;

import cool.scx.ScxHandlerRE;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * a
 *
 * @param <T> t
 * @author scx567888
 * @version 1.7.3
 */
public class ScalarHandler<T> implements ScxHandlerRE<ResultSet, T, SQLException> {

    /**
     * a
     */
    private final int columnIndex;

    /**
     * a
     */
    private final String columnName;

    /**
     * a
     */
    private final Class<T> clazz;

    /**
     * 使用名称还是索引
     */
    private final boolean useName;

    /**
     * <p>Constructor for ScalarHandler.</p>
     *
     * @param columnIndex a
     */
    public ScalarHandler(int columnIndex, Class<T> clazz) {
        this.columnIndex = columnIndex;
        this.columnName = null;
        this.useName = false;
        this.clazz = clazz;
    }

    /**
     * <p>Constructor for ScalarHandler.</p>
     *
     * @param columnName a
     */
    public ScalarHandler(String columnName, Class<T> clazz) {
        this.columnIndex = 0;
        this.columnName = columnName;
        this.useName = true;
        this.clazz = clazz;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T handle(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return useName ? rs.getObject(this.columnName, clazz) : rs.getObject(this.columnIndex, clazz);
        }
        return null;
    }

}
