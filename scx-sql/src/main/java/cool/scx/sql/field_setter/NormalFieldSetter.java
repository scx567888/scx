package cool.scx.sql.field_setter;

import cool.scx.sql.FieldSetter;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>BeanBuilder interface.</p>
 *
 * @author scx567888
 * @version 0.2.1
 */
public class NormalFieldSetter extends FieldSetter {

    public NormalFieldSetter(Field field) {
        super(field);
    }

    @Override
    public Object getObject(ResultSet rs, int index) throws SQLException {
        return rs.getObject(index, field.getType());
    }

}