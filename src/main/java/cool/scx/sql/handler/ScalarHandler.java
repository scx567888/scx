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
    public ScalarHandler() {
        this(1, null);
    }

    /**
     * <p>Constructor for ScalarHandler.</p>
     *
     * @param columnIndex a
     */
    public ScalarHandler(int columnIndex) {
        this(columnIndex, null);
    }

    /**
     * <p>Constructor for ScalarHandler.</p>
     *
     * @param columnName a
     */
    public ScalarHandler(String columnName) {
        this(1, columnName);
    }

    /**
     * <p>Constructor for ScalarHandler.</p>
     *
     * @param columnIndex a
     * @param columnName  a
     */
    private ScalarHandler(int columnIndex, String columnName) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public T handle(ResultSet rs) throws SQLException {

        if (rs.next()) {
            if (this.columnName == null) {
                return (T) rs.getObject(this.columnIndex);
            }
            return (T) rs.getObject(this.columnName);
        }
        return null;
    }

}
