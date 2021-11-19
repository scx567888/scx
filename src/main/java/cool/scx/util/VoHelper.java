package cool.scx.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * 普通的 objectMapper 用于向前台发送数据使用
     */
    private static final ObjectMapper OBJECT_MAPPER = JacksonHelper.initObjectMapper();

    /**
     * 将 一个对象转换为 json 字符串 (这里本质上是调用了)
     * <p>
     * 所以其中标注的所有注解均会执行 , 包括 @JsonIgnore
     * 也就是说如果想向前台发送一些 json 建议使用此方法进行序列化
     * 以保证标注 @JsonIgnore 注解的属性可以被忽略 防止一些私密信息泄露
     *
     * @param value object
     * @return string
     */
    public static String toJson(Object value) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(value);
    }

    /**
     * 填充 CONTENT_TYPE 为 json 格式
     *
     * @param response r
     * @return r
     */
    public static HttpServerResponse fillJsonContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=utf-8");
    }

    public static HttpServerResponse fillHtmlContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
    }

    public static HttpServerResponse fillTextPlainContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
    }

    /**
     * 填充 contextType
     *
     * @param contentType c
     * @param response    r
     */
    public static HttpServerResponse fillContentType(String contentType, HttpServerResponse response) {
        if (contentType != null) {
            if (contentType.startsWith("text")) {
                return response.putHeader(HttpHeaderNames.CONTENT_TYPE, contentType + ";charset=utf-8");
            } else {
                return response.putHeader(HttpHeaderNames.CONTENT_TYPE, contentType);
            }
        } else {
            return response.putHeader(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
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
