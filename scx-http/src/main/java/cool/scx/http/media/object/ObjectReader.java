package cool.scx.http.media.object;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import cool.scx.common.util.ObjectUtils;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

import static cool.scx.http.media.json_node.JsonNodeReader.JSON_NODE_READER;

/// ObjectReader
///
/// @author scx567888
/// @version 0.0.1
public class ObjectReader<T> implements MediaReader<T> {

    private final JavaType type;

    public ObjectReader(Class<T> clazz) {
        this.type = ObjectUtils.constructType(clazz);
    }

    public ObjectReader(TypeReference<T> clazz) {
        this.type = ObjectUtils.constructType(clazz);
    }

    public ObjectReader(JavaType clazz) {
        this.type = clazz;
    }

    @Override
    public T read(InputStream inputStream, ScxHttpHeaders requestHeaders) {
        // 这里我们直接使用 JSON_NODE_READER 来进行解析
        var jsonNode = JSON_NODE_READER.read(inputStream, requestHeaders);
        return ObjectUtils.convertValue(jsonNode, type);
    }

}
