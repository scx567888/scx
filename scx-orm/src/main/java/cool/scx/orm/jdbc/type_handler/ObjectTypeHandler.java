package cool.scx.orm.jdbc.type_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.scx.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ObjectTypeHandler implements TypeHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ObjectTypeHandler.class);

    private static final ObjectMapper objectMapper = ObjectUtils.jsonMapper(ObjectUtils.Option.IGNORE_JSON_IGNORE);

    private final JavaType javaType;

    public ObjectTypeHandler(Type type) {
        this.javaType = ObjectUtils.constructType(type);
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Object parameter) throws SQLException {
        try {
            var json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (JsonProcessingException e) {
            logger.error("序列化时发生错误 , 已使用 NULL !!!", e);
            ps.setNull(i, Types.NULL);
        }
    }

    @Override
    public Object getObject(ResultSet rs, int index) throws SQLException {
        var json = rs.getString(index);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            logger.error("反序列化时发生错误 , 已使用 NULL !!!", e);
            return null;
        }
    }

}
