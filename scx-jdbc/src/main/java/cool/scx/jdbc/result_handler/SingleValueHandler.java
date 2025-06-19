package cool.scx.jdbc.result_handler;

import cool.scx.jdbc.dialect.Dialect;

import java.sql.ResultSet;
import java.sql.SQLException;

/// SingleValueHandler
///
/// @param <T> t
/// @author scx567888
/// @version 0.0.1
final class SingleValueHandler<T> implements ResultHandler<T, RuntimeException> {

    /// 列索引
    private final int columnIndex;

    /// 列名
    private final String columnName;

    /// 所需 class
    private final Class<T> clazz;

    /// 使用名称还是索引
    private final boolean useName;

    public SingleValueHandler() {
        this(1, null);
    }

    public SingleValueHandler(Class<T> clazz) {
        this(1, clazz);
    }

    public SingleValueHandler(int columnIndex) {
        this(columnIndex, null);
    }

    public SingleValueHandler(String columnName) {
        this(columnName, null);
    }

    public SingleValueHandler(int columnIndex, Class<T> clazz) {
        this(columnIndex, null, clazz, false);
    }

    public SingleValueHandler(String columnName, Class<T> clazz) {
        this(1, columnName, clazz, true);
    }

    private SingleValueHandler(int columnIndex, String columnName, Class<T> clazz, boolean useName) {
        this.columnIndex = columnIndex;
        this.columnName = columnName;
        this.clazz = clazz;
        this.useName = useName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T apply(ResultSet rs, Dialect dialect) throws SQLException {
        if (rs.next()) {
            if (this.clazz == null) {
                return (T) (this.useName ? rs.getObject(this.columnName) : rs.getObject(this.columnIndex));
            } else {
                return this.useName ? rs.getObject(this.columnName, clazz) : rs.getObject(this.columnIndex, clazz);
            }
        } else {
            return null;
        }
    }

}
