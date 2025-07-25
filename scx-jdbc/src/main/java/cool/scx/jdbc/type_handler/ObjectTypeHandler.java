package cool.scx.jdbc.type_handler;

import cool.scx.object.ScxObject;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.parser.NodeParseException;
import cool.scx.object.serializer.NodeSerializeException;
import cool.scx.reflect.ScxReflect;
import cool.scx.reflect.TypeInfo;

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

    private final TypeInfo javaType;

    public ObjectTypeHandler(Type type) {
        this.javaType = ScxReflect.typeOf(type);
    }

    public ObjectTypeHandler(TypeInfo type) {
        this.javaType = type;
    }

    @Override
    public void setObject(PreparedStatement ps, int i, Object parameter) throws SQLException {
        try {
            var json = ScxObject.toJson(parameter);
            ps.setString(i, json);
        } catch (NodeMappingException | NodeSerializeException e) {
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
            return ScxObject.fromJson(json, javaType);
        } catch (NodeMappingException | NodeParseException e) {
            logger.log(ERROR, "反序列化时发生错误 , 已使用 NULL !!!", e);
            return null;
        }
    }

}
