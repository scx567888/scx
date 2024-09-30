package cool.scx.http.media.object;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.ScxHttpHeadersWritable;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.content_type.ContentTypeWritable;
import cool.scx.http.media.MediaWriter;

import java.io.OutputStream;

import static cool.scx.common.util.ObjectUtils.toJson;
import static cool.scx.common.util.ObjectUtils.toXml;
import static cool.scx.http.MediaType.APPLICATION_JSON;
import static cool.scx.http.MediaType.APPLICATION_XML;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ObjectWriter implements MediaWriter {

    private final Object object;
    private byte[] data;

    public ObjectWriter(Object object) {
        this.object = object;
    }

    public static ContentTypeWritable trySetContentType(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers) {
        var x = ContentType.of(APPLICATION_XML).charset(UTF_8);
        var j = ContentType.of(APPLICATION_JSON).charset(UTF_8);
        //尝试设置 contentType
        if (headersWritable.contentType() == null) {
            var accepts = headers.accepts();
            // 只有明确指定 接受参数是 application/xml 的才返回 xml
            if (accepts != null) {
                for (var accept : accepts) {
                    if (accept.mediaType() == APPLICATION_XML) {
                        headersWritable.contentType(x);
                        return x;
                    }
                }
            }
            headersWritable.contentType(j);
        }
        return headersWritable.contentType();
    }

    @Override
    public void beforeWrite(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers) {
        //尝试设置 contentType
        var ccc = trySetContentType(headersWritable, headers);
        //根据类型确定内容长度
        try {
            if (ccc.mediaType() == APPLICATION_JSON) {
                data = toJson(object).getBytes();
            } else if (ccc.mediaType() == APPLICATION_XML) {
                data = toXml(object).getBytes();
            } else {
                throw new RuntimeException("Unsupported media type: " + ccc.mediaType());
            }
        } catch (Exception e) {
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
