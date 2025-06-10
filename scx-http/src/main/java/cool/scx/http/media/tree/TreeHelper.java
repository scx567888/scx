package cool.scx.http.media.tree;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media_type.ScxMediaType;

import static cool.scx.http.media_type.MediaType.APPLICATION_JSON;
import static cool.scx.http.media_type.MediaType.APPLICATION_XML;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class TreeHelper {

    /// 根据客户端的 Accept 值尝试推断我们应该使用什么类型的 Content-Type
    public static ScxMediaType trySetContentType(ScxHttpHeadersWritable headersWritable, ScxHttpHeaders headers) {
        //1,用户端已经明确设置了 contentType 则跳过后续判断
        if (headersWritable.contentType() != null) {
            return headersWritable.contentType();
        }

        var accept = headers.accept();
        // 如果客户端未指明 Accepts 则返回 JSON 
        if (accept == null) {
            headersWritable.contentType(ScxMediaType.of(APPLICATION_JSON).charset(UTF_8));
            return headersWritable.contentType();
        }

        //测试 JSON 或者 XML  
        var mediaType = accept.negotiate(APPLICATION_JSON, APPLICATION_XML);
        if (mediaType == APPLICATION_XML) {
            headersWritable.contentType(ScxMediaType.of(APPLICATION_XML).charset(UTF_8));
        } else {
            //否则回退到 JSON 
            headersWritable.contentType(ScxMediaType.of(APPLICATION_JSON).charset(UTF_8));
        }
        return headersWritable.contentType();
    }

}
