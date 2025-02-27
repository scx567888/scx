package cool.scx.jdbc.type_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.scx.common.util.ObjectUtils;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static java.lang.System.Logger.Level.ERROR;

/// ObjectTypeHandler 默认都会转换为 json 字符串来存储
///
/// @author scx567888
/// @version 0.0.1
public class ObjectTypeHandler implements TypeHandler<Object> {

    private static final System.Logger logger = System.getLogger(ObjectTypeHandler.class.getName());

    private static final ObjectMapper objectMapper = ObjectUtils.jsonMapper(new ObjectUtils.Options().setIgnoreJsonIgnore(true));

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
            logger.log(ERROR, "序列化时发生错误 , 已使用 NULL !!!", e);
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
            logger.log(ERROR, "反序列化时发生错误 , 已使用 NULL !!!", e);
            return null;
        }
    }

}
