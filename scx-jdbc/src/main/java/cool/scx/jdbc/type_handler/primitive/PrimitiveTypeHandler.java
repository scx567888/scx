package cool.scx.jdbc.type_handler.primitive;

import cool.scx.jdbc.type_handler.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/// 处理原始类型 因为原始类型 不能为空 (必须有一个值) 所以使用此类支持 默认值处理
///
/// @param <T>
/// @author scx567888
/// @version 0.0.1
abstract class PrimitiveTypeHandler<T> implements TypeHandler<T> {

    final boolean isPrimitive;

    final T defaultValue;

    PrimitiveTypeHandler(boolean isPrimitive, T defaultValue) {
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
