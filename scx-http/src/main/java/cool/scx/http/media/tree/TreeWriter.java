package cool.scx.http.media.tree;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.io.ByteOutput;
import cool.scx.object.ScxObject;
import cool.scx.object.node.Node;
import cool.scx.object.serializer.NodeSerializeException;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.media.tree.TreeHelper.trySetContentType;
import static cool.scx.http.media_type.MediaType.APPLICATION_JSON;
import static cool.scx.http.media_type.MediaType.APPLICATION_XML;
import static java.nio.charset.StandardCharsets.UTF_8;

/// JsonNodeWriter
///
/// @author scx567888
/// @version 0.0.1
public class TreeWriter implements MediaWriter {

    private final Node jsonNode;
    private byte[] data;

    public TreeWriter(Node jsonNode) {
        this.jsonNode = jsonNode;
        this.data = null;
    }

    @Override
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        var contentType = trySetContentType(responseHeaders, requestHeaders);
        //根据类型确定内容长度
        try {
            if (APPLICATION_JSON.equalsIgnoreParams(contentType)) {
                // 这里直接 writeValueAsBytes 的话会导致 emoji 表情符被 转义 所以这里转换成 字符串 然后在处理
                data = ScxObject.toJson(jsonNode).getBytes(UTF_8);
            } else if (APPLICATION_XML.equalsIgnoreParams(contentType)) {
                data = ScxObject.toXml(jsonNode).getBytes(UTF_8);
            } else {
                //这里 表示用户设置的 类型 既不是 JSON 也不是 XML 我们无法处理 抛出异常
                throw new IllegalArgumentException("Unsupported media type: " + contentType);
            }
        } catch (NodeSerializeException e) {
            //这里表示用户的 jsonNode 无法被转换为字符串 (比如递归引用) 这里抛出异常
            throw new IllegalArgumentException(e);
        }
        return data.length;
    }

    @Override
    public void write(ByteOutput byteOutput) throws IOException {
        try (byteOutput) {
            byteOutput.write(data);
        }
    }

}
