package cool.scx.http.media.object;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;

import java.io.OutputStream;

import static cool.scx.common.util.ObjectUtils.toJson;
import static cool.scx.common.util.ObjectUtils.toXml;
import static cool.scx.http.media.json_node.JsonNodeWriter.trySetContentType;
import static cool.scx.http.media_type.MediaType.APPLICATION_JSON;
import static cool.scx.http.media_type.MediaType.APPLICATION_XML;
import static java.nio.charset.StandardCharsets.UTF_8;

/// ObjectWriter
///
/// @author scx567888
/// @version 0.0.1
public class ObjectWriter implements MediaWriter {

    private final Object object;
    private byte[] data;

    public ObjectWriter(Object object) {
        this.object = object;
        this.data = null;
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        var contentType = trySetContentType(responseHeaders, requestHeaders);
        //根据类型确定内容长度
        try {
            if (APPLICATION_JSON.equalsIgnoreParams(contentType)) {
                // 这里直接 writeValueAsBytes 的话会导致 emoji 表情符被 转义 所以这里转换成 字符串 然后在处理
                data = toJson(object).getBytes(UTF_8);
            } else if (APPLICATION_XML.equalsIgnoreParams(contentType)) {
                data = toXml(object).getBytes(UTF_8);
            } else {
                throw new IllegalArgumentException("Unsupported media type: " + contentType);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (responseHeaders.contentLength() == null) {
            responseHeaders.contentLength(data.length);
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
