package cool.scx.http.media.object;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.media.tree.TreeWriter;
import cool.scx.io.ByteOutput;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;
import cool.scx.object.mapping.NodeMappingException;
import cool.scx.object.serializer.NodeSerializeException;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.media.tree.TreeHelper.trySetContentType;
import static cool.scx.http.media_type.MediaType.APPLICATION_JSON;
import static cool.scx.http.media_type.MediaType.APPLICATION_XML;
import static cool.scx.object.ScxObject.toJson;
import static cool.scx.object.ScxObject.toXml;
import static java.nio.charset.StandardCharsets.UTF_8;

/// ObjectWriter  逻辑参考 {@link TreeWriter}
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
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        var contentType = trySetContentType(responseHeaders, requestHeaders);
        //根据类型确定内容长度
        try {
            if (APPLICATION_JSON.equalsIgnoreParams(contentType)) {
                // 这里直接 writeValueAsBytes 的话会导致 emoji 表情符被 转义 所以这里转换成 字符串 然后在处理
                data = toJson(object).getBytes(UTF_8);
            } else if (APPLICATION_XML.equalsIgnoreParams(contentType)) {
                data = toXml(object).getBytes(UTF_8);
            } else {
                //这里 表示用户设置的 类型 既不是 JSON 也不是 XML 我们无法处理 抛出异常
                throw new IllegalArgumentException("Unsupported media type: " + contentType);
            }
        } catch (NodeMappingException | NodeSerializeException e) {
            //这里表示用户的 jsonNode 无法被转换为字符串 (比如递归引用) 这里抛出异常
            throw new IllegalArgumentException(e);
        }
        return data.length;
    }

    @Override
    public void write(ByteOutput byteOutput) throws ScxIOException, AlreadyClosedException {
        try (byteOutput) {
            byteOutput.write(data);
        }
    }

}
