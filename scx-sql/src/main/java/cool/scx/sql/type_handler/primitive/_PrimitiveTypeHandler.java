package cool.scx.sql.type_handler.primitive;

import cool.scx.sql.type_handler.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract class _PrimitiveTypeHandler<T> implements TypeHandler<T> {

    final boolean isPrimitive;

    final T nullValue;

    _PrimitiveTypeHandler(boolean isPrimitive, T nullValue) {
        this.isPrimitive = isPrimitive;
        this.nullValue = nullValue;
    }

    @Override
    public final T getObject(ResultSet rs, int index) throws SQLException {
        T object = getObject0(rs, index);
        if (rs.wasNull()) {
            object = null;
        }
        if (isPrimitive && object == null) {
            return nullValue;
        }
        return object;
    }

    @Override
    public T getNullValue() {
        return nullValue;
    }

    public abstract T getObject0(ResultSet rs, int index) throws SQLException;

}
