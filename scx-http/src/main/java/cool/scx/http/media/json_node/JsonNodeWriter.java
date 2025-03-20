package cool.scx.http.media.json_node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media_type.ScxMediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static cool.scx.common.util.ObjectUtils.xmlMapper;
import static cool.scx.http.media_type.MediaType.APPLICATION_JSON;
import static cool.scx.http.media_type.MediaType.APPLICATION_XML;
import static java.nio.charset.StandardCharsets.UTF_8;

/// JsonNodeWriter
///
/// @author scx567888
/// @version 0.0.1
public class JsonNodeWriter implements MediaWriter {

    private final JsonNode jsonNode;
    private byte[] data;

    public JsonNodeWriter(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
        this.data = null;
    }

    public static ScxMediaType trySetContentType(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers) {
        //已经设置了则跳过设置
        if (headersWritable.contentType() != null) {
            return headersWritable.contentType();
        }
        var accept = headers.accept();
        // 如果客户端未指明 Accepts 则返回 JSON 
        if (accept == null) {
            headersWritable.contentType(ScxMediaType.of(APPLICATION_JSON).charset(UTF_8));
            return headersWritable.contentType();
        }
        //测试 XML 或者 JSON 
        var mediaType = accept.negotiate(APPLICATION_JSON, APPLICATION_XML);
        if (mediaType == APPLICATION_XML) {
            headersWritable.contentType(ScxMediaType.of(APPLICATION_XML).charset(UTF_8));
        } else {
            //否则回退到 JSON 
            headersWritable.contentType(ScxMediaType.of(APPLICATION_JSON).charset(UTF_8));
        }
        return headersWritable.contentType();
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        var contentType = trySetContentType(responseHeaders, requestHeaders);
        //根据类型确定内容长度
        try {
            if (APPLICATION_JSON.equalsIgnoreParams(contentType)) {
                // 这里直接 writeValueAsBytes 的话会导致 emoji 表情符被 转义 所以这里转换成 字符串 然后在处理
                data = jsonMapper().writeValueAsString(jsonNode).getBytes(UTF_8);
            } else if (APPLICATION_XML.equalsIgnoreParams(contentType)) {
                data = xmlMapper().writeValueAsString(jsonNode).getBytes(UTF_8);
            } else {
                //这里 表示用户设置的 类型 既不是 JSON 也不是 XML 我们无法处理 抛出异常
                throw new IllegalArgumentException("Unsupported media type: " + contentType);
            }
        } catch (JsonProcessingException e) {
            //这里表示用户的 jsonNode 无法被转换为字符串 (比如递归引用) 这里抛出异常
            throw new IllegalArgumentException(e);
        }
        if (responseHeaders.contentLength() == null) {
            responseHeaders.contentLength(data.length);
        }
    }

    @Override
    public void write(OutputStream outputStream) {
        try (outputStream) {
            outputStream.write(data);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
