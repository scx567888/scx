package cool.scx.sql.field_setter;


import cool.scx.sql.FieldSetter;
import cool.scx.sql.SQLHelper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public class JsonFieldSetter extends FieldSetter {

    public JsonFieldSetter(Field field) {
        super(field);
    }

    @Override
    public Object getObject(ResultSet s, int index) throws SQLException {
        return SQLHelper.readFromJsonValueOrNull(s.getString(index), fieldGenericType);
    }

}