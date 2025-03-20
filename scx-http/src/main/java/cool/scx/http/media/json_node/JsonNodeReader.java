package cool.scx.http.media.json_node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static cool.scx.common.util.ObjectUtils.xmlMapper;
import static cool.scx.http.media.string.StringReader.STRING_READER;
import static cool.scx.http.media_type.MediaType.APPLICATION_JSON;
import static cool.scx.http.media_type.MediaType.APPLICATION_XML;

/// JsonNodeReader
///
/// @author scx567888
/// @version 0.0.1
public class JsonNodeReader implements MediaReader<JsonNode> {

    public static final JsonNodeReader JSON_NODE_READER = new JsonNodeReader();

    private JsonNodeReader() {

    }

    @Override
    public JsonNode read(InputStream inputStream, ScxHttpHeaders requestHeaders) {
        // 1, 先读取为字符串
        var str = STRING_READER.read(inputStream, requestHeaders);
        // 2, 根据不同 contentType 进行处理
        var contentType = requestHeaders.contentType();
        // 尝试 JSON
        if (APPLICATION_JSON.equalsIgnoreParams(contentType)) {
            try {
                return jsonMapper().readTree(str);
            } catch (JsonProcessingException e) {
                // 这里既然客户端已经 指定了 contentType 为 JSON 我们却无法转换 说明 客户端发送的 内容格式可能有误 
                // 所以这里 抛出 客户端错误 BadRequestException
                throw new BadRequestException("JSON 格式不正确 !!!", e);
            }
        }
        // 尝试 XML
        if (APPLICATION_XML.equalsIgnoreParams(contentType)) {
            try {
                return xmlMapper().readTree(str);
            } catch (JsonProcessingException e) {
                // 同上
                throw new BadRequestException("XML 格式不正确 !!!", e);
            }
        }

        //JSON 和 XML 均不匹配 进行猜测
        try { //先尝试以 JSON 格式进行尝试转换
            return jsonMapper().readTree(str);
        } catch (Exception exception) {
            try {//再尝试以 XML 的格式进行转换
                return xmlMapper().readTree(str);
            } catch (JsonProcessingException e) {
                // JSON 和 XML 均转换失败 直接报错
                // 这里因为客户端没有指定格式 所以不能抛出 BadRequestException 这种客户端错误 而是应该抛出内部错误
                throw new IllegalArgumentException("无法转换为 JsonNode !!! : " + str);
            }
        }
    }

}
