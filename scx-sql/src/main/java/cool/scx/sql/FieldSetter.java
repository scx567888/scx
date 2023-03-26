package cool.scx.sql;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public final class FieldSetter {

    private final Field javaField;
    private final String columnName;
    private final TypeHandler<?> typeHandler;

    public FieldSetter(Field field, String columnName) {
        this.javaField = field;
        this.columnName = columnName;
        var fieldGenericType = field.getGenericType();
        this.javaField.setAccessible(true);
        this.typeHandler = TypeHandlerRegistry.getTypeHandler(fieldGenericType);
    }

    public void set(Object t, ResultSet s, int index) throws SQLException {
        var o = getObject(s, index);
        if (o != null) {
            try {
                this.javaField.set(t, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String columnName() {
        return columnName;
    }

    public Field javaField() {
        return javaField;
    }

    public Object getObject(ResultSet s, int index) throws SQLException {
        return typeHandler.getObject(s, index);
    }

}