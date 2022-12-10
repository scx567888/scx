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
public class EnumFieldSetter extends FieldSetter {

    public EnumFieldSetter(Field field) {
        super(field);
    }

    @Override
    public Object getObject(ResultSet rs, int index) throws SQLException {
        return SQLHelper.readFromValueOrNull(rs.getString(index), field.getType());
    }

}