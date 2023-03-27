package cool.scx.sql.type_handler.primitive;

import cool.scx.sql.TypeHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理 java 中基类不能为空的状况
 *
 * @param <T> 基类的包装类
 */
abstract class _PrimitiveTypeHandler<T> implements TypeHandler<T> {

    private final boolean isPrimitive;

    protected _PrimitiveTypeHandler(boolean isPrimitive) {
        this.isPrimitive = isPrimitive;
    }

    @Override
    public final T getObject(ResultSet rs, int index) throws SQLException {
        //这里 result 必须不能为空
        var result = getPrimitiveObject(rs, index);
        //基类就直接返回,
        if (isPrimitive) {
            return result;
        } else {//如果是包装类就表示允许 null 的存在
            return rs.wasNull() ? null : result;
        }
    }

    /**
     * 获取基类 Object (返回值不允许空)
     *
     * @param rs    rs
     * @param index index
     * @return 基类值
     * @throws SQLException sql
     */
    public abstract T getPrimitiveObject(ResultSet rs, int index) throws SQLException;

}
