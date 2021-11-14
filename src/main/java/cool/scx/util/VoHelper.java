package cool.scx.util;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.HttpServerResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * vo helper
 *
 * @author scx567888
 * @version 1.4.4
 */
public final class VoHelper {

    /**
     * 填充 contextType
     *
     * @param contentType c
     * @param response    r
     */
    public static void fillContentType(String contentType, HttpServerResponse response) {
        if (contentType != null) {
            if (contentType.startsWith("text")) {
                response.putHeader(HttpHeaderNames.CONTENT_TYPE, contentType + ";charset=utf-8");
            } else {
                response.putHeader(HttpHeaderNames.CONTENT_TYPE, contentType);
            }
        } else {
            response.putHeader(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
        }
    }

    /**
     * todo 这是一个 hack
     * URLEncoder.encode 针对 ' ' (空格) 会编码为 '+' , 而这里我们需要的是编码为 %20
     *
     * @param downloadName a {@link java.lang.String} object
     * @return c
     */
    public static String getDownloadContentDisposition(String downloadName) {
        return "attachment;filename*=utf-8''" + URLEncoder.encode(downloadName, StandardCharsets.UTF_8).replace("+", "%20");
    }

}
