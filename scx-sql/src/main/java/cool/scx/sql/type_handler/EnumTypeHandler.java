package cool.scx.sql.type_handler;

import cool.scx.sql.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//todo test
public class EnumTypeHandler<E extends Enum<E>> implements TypeHandler<E> {

    private static final Logger logger = LoggerFactory.getLogger(EnumTypeHandler.class);

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
        String s = rs.getString(index);
        if (s == null) {
            return null;
        }
        try {
            return Enum.valueOf(type, s);
        } catch (Exception e) {
            logger.error("枚举转换出现错误 : ", e);
            return null;
        }
    }

}
