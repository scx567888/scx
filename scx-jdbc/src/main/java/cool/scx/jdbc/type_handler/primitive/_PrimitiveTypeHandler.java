package cool.scx.jdbc.type_handler.primitive;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract class _PrimitiveTypeHandler<T> implements TypeHandler<T> {

    final boolean isPrimitive;

    final T defaultValue;

    _PrimitiveTypeHandler(boolean isPrimitive, T defaultValue) {
        this.isPrimitive = isPrimitive;
        // 处理基类以外全使用 null
        this.defaultValue = isPrimitive ? defaultValue : null;
    }

    @Override
    public final T getObject(ResultSet rs, int index) throws SQLException {
        T object = getObject0(rs, index);
        if (rs.wasNull()) {
            object = null;
        }
        // 基类不允许为空 所以我们在检测到基类为空时 返回默认值
        if (isPrimitive && object == null) {
            return defaultValue;
        }
        return object;
    }

    @Override
    public final T getDefaultValue() {
        return defaultValue;
    }

    public abstract T getObject0(ResultSet rs, int index) throws SQLException;

}
