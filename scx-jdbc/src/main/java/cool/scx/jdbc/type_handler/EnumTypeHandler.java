package cool.scx.jdbc.type_handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.System.Logger.Level.ERROR;

public class EnumTypeHandler<E extends Enum<E>> implements TypeHandler<E> {

    private static final System.Logger logger = System.getLogger(EnumTypeHandler.class.getName());

    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setObject(PreparedStatement ps, int i, E parameter) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public E getObject(ResultSet rs, int index) throws SQLException {
        String name = rs.getString(index);
        if (name == null) {
            return null;
        }
        try {
            return Enum.valueOf(type, name);
        } catch (Exception e) {
            logger.log(ERROR, "枚举转换出现错误 : ", e);
            return null;
        }
    }

}
