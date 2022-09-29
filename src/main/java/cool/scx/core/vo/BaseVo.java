package cool.scx.core.vo;

import cool.scx.functional.ScxHandlerAE;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * BaseVo 接口
 * 所有需要向前台返回数据都需要继承
 *
 * @author scx567888
 * @version 0.5.0
 */
public interface BaseVo extends ScxHandlerAE<RoutingContext, Exception> {

    /**
     * 填充 ContentType a
     *
     * @param contentType a
     * @param response    a
     * @return a
     */
    static HttpServerResponse fillContentType(String contentType, HttpServerResponse response) {
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
     * 填充 CONTENT_TYPE 为 json 格式
     *
     * @param response r
     * @return r
     */
    static HttpServerResponse fillJsonContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeaderNames.CONTENT_TYPE, "application/json;charset=utf-8");
    }

    /**
     * 填充 CONTENT_TYPE 为 xml 格式
     *
     * @param response r
     * @return r
     */
    static HttpServerResponse fillXmlContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeaderNames.CONTENT_TYPE, "application/xml;charset=utf-8");
    }

    /**
     * a
     *
     * @param response a
     * @return a
     */
    static HttpServerResponse fillHtmlContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
    }

    /**
     * a
     *
     * @param response a
     * @return a
     */
    static HttpServerResponse fillTextPlainContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
    }

    /**
     * todo 这是一个 hack
     * URLEncoder.encode 针对 ' ' (空格) 会编码为 '+' , 而这里我们需要的是编码为 %20
     *
     * @param downloadName a {@link java.lang.String} object
     * @return c
     */
    static String getDownloadContentDisposition(String downloadName) {
        return "attachment;filename*=utf-8''" + URLEncoder.encode(downloadName, StandardCharsets.UTF_8).replace("+", "%20");
    }

}
