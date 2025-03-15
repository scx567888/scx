package cool.scx.http.media.json_node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static cool.scx.common.util.ObjectUtils.xmlMapper;
import static cool.scx.http.MediaType.APPLICATION_JSON;
import static cool.scx.http.MediaType.APPLICATION_XML;
import static cool.scx.http.media.string.StringReader.STRING_READER;

/// JsonNodeReader
///
/// @author scx567888
/// @version 0.0.1
public class JsonNodeReader implements MediaReader<JsonNode> {

    public static final JsonNodeReader JSON_NODE_READER = new JsonNodeReader();

    @Override
    public JsonNode read(InputStream inputStream, ScxHttpHeaders requestHeaders) {
        var str = STRING_READER.read(inputStream, requestHeaders);
        var contentType = requestHeaders.contentType();
        var mediaType = contentType != null ? contentType.mediaType() : null;
        //猜测一下
        if (APPLICATION_JSON.equals(mediaType)) {
            try {
                return jsonMapper().readTree(str);
            } catch (JsonProcessingException e) {
                throw new BadRequestException(e);
            }
        }
        if (APPLICATION_XML.equals(mediaType)) {
            try {
                return xmlMapper().readTree(str);
            } catch (JsonProcessingException e) {
                throw new BadRequestException(e);
            }
        }
        try { //先尝试以 json 格式进行尝试转换
            return jsonMapper().readTree(str);
        } catch (Exception exception) {
            try {//再尝试以 xml 的格式进行转换
                return xmlMapper().readTree(str);
            } catch (JsonProcessingException e) {
                // json 和 xml 均转换失败 直接报错
                throw new IllegalArgumentException(str);
            }
        }
    }

}
