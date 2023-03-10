package cool.scx.sql.type_handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import cool.scx.sql.TypeHandler;
import cool.scx.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectTypeHandler implements TypeHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ObjectTypeHandler.class);

    private static final ObjectMapper objectMapper = ObjectUtils.jsonMapper(ObjectUtils.Option.IGNORE_JSON_IGNORE);
    private final Type type;

    public ObjectTypeHandler(Type type) {
        this.type = type;
    }

    /**
     * a
     *
     * @param o a
     * @return a
     */
    public static String convertToStringOrNull(Object o) {
        try {
            return objectMapper.convertValue(o, String.class);
        } catch (Exception e) {
            logger.error("序列化时发生错误 , 已使用 NULL !!!", e);
            return null;
        }
    }

    /**
     * a
     *
     * @param o a
     * @return a
     */
    public static String convertToJsonOrNull(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            logger.error("序列化时发生错误 , 已使用 NULL !!!", e);
            return null;
        }
    }

    /**
     * a
     *
     * @param o         a
     * @param filedType a
     * @return a
     */
    public static Object readFromValueOrNull(String o, Class<?> filedType) {
        if (o != null) {
            try {
                return objectMapper.convertValue(o, filedType);
            } catch (Exception e) {
                logger.error("反序列化时发生错误 , 已使用 NULL !!!", e);
            }
        }
        return null;
    }

    /**
     * 读取 json 值 或者返回 null
     *
     * @param json        s
     * @param genericType g
     * @return r
     */
    public static Object readFromJsonValueOrNull(String json, Type genericType) {
        if (json != null) {
            try {
                return objectMapper.readValue(json, ObjectUtils.constructType(genericType));
            } catch (Exception e) {
                logger.error("反序列化时发生错误 , 已使用 NULL !!!", e);
            }
        }
        return null;
    }

    @Override
    public Object getObject(ResultSet rs, int index) throws SQLException {
        return readFromJsonValueOrNull(rs.getString(index), type);
    }

}
