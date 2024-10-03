package cool.scx.http.media.json_node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.content_type.ContentTypeWritable;
import cool.scx.http.media.MediaWriter;

import java.io.OutputStream;

import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static cool.scx.common.util.ObjectUtils.xmlMapper;
import static cool.scx.http.MediaType.APPLICATION_JSON;
import static cool.scx.http.MediaType.APPLICATION_XML;
import static java.nio.charset.StandardCharsets.UTF_8;

public class JsonNodeWriter implements MediaWriter {

    private final JsonNode jsonNode;
    private byte[] data;

    public JsonNodeWriter(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
        this.data = null;
    }

    public static ContentTypeWritable trySetContentType(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers) {
        //已经设置了则跳过设置
        if (headersWritable.contentType() != null) {
            return headersWritable.contentType();
        }
        var accepts = headers.accepts();
        // 如果客户端未指明 accepts 则返回 json 
        if (accepts == null) {
            headersWritable.contentType(ContentType.of(APPLICATION_JSON).charset(UTF_8));
            return headersWritable.contentType();
        }
        //如果 拥有 XML 或者 JSON 则返回二者其一
        for (var accept : accepts) {
            if (accept.mediaType() == APPLICATION_XML) {
                headersWritable.contentType(ContentType.of(APPLICATION_XML).charset(UTF_8));
                return headersWritable.contentType();
            } else if (accept.mediaType() == APPLICATION_JSON) {
                headersWritable.contentType(ContentType.of(APPLICATION_JSON).charset(UTF_8));
                return headersWritable.contentType();
            }
        }
        //否则回退到 JSON 
        headersWritable.contentType(ContentType.of(APPLICATION_JSON).charset(UTF_8));
        return headersWritable.contentType();
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers) {
        var contentType = trySetContentType(headersWritable, headers);
        //根据类型确定内容长度
        try {
            if (contentType.mediaType() == APPLICATION_JSON) {
                data = jsonMapper().writeValueAsBytes(jsonNode);
            } else if (contentType.mediaType() == APPLICATION_XML) {
                data = xmlMapper().writeValueAsBytes(jsonNode);
            } else {
                throw new IllegalArgumentException("Unsupported media type: " + contentType.mediaType());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (headersWritable.contentLength() == null) {
            headersWritable.contentLength(data.length);
        }
    }

    @Override
    public void write(OutputStream outputStream) {
        try (outputStream) {
            outputStream.write(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
