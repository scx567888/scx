package cool.scx.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 针对 HashMap 中可能出现的 null key 这里做特殊处理
 *
 * @author scx567888
 * @version 0.0.1
 */
public class NullKeySerializer extends JsonSerializer<Object> {

    /**
     * 默认的 NullKey 序列化器
     */
    public static final NullKeySerializer NULL_KEY_SERIALIZER = new NullKeySerializer("");

    private final String nullKey;

    public NullKeySerializer(String nullKey) {
        this.nullKey = nullKey;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName(nullKey);
    }

}
