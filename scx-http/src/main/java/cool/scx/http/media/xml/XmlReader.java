package cool.scx.http.media.xml;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.IOException;
import java.io.InputStream;

import static cool.scx.common.util.ObjectUtils.xmlMapper;
import static cool.scx.http.media.string.StringReader.STRING_READER;

/// XML Reader 默认先将请求体读取为字符串，然后解析为 JsonNode。
/// 原因说明：
/// - 在大多数 Web 场景中，请求体大小通常 < 200KB；
/// - 经测试，在这种数据量下，先读取为字符串的方式通常性能更好（避免流式初始化、便于字符集处理）；
/// - 同时具有更强的容错性和调试便利性（可打印原始字符串）；
/// 若后续需要处理大 XML（如 >1MB）或高并发大吞吐场景，
/// 可考虑替换为基于 InputStream 的流式解析器。
///
/// @author scx567888
/// @version 0.0.1
public class XmlReader implements MediaReader<JsonNode> {

    public static final XmlReader XML_READER = new XmlReader();

    private XmlReader() {

    }

    @Override
    public JsonNode read(InputStream inputStream, ScxHttpHeaders requestHeaders) throws IOException {
        // 1, 先读取为字符串
        var str = STRING_READER.read(inputStream, requestHeaders);

        try {
            //以 XML 格式进行尝试转换
            return xmlMapper().readTree(str);
        } catch (Exception exception) {
            // XML 转换失败 直接报错
            // 这里因为客户端没有指定格式 所以不能抛出 BadRequestException 这种客户端错误 而是应该抛出内部错误
            throw new IllegalArgumentException("无法转换为 JsonNode !!! : " + str);
        }

    }

}
