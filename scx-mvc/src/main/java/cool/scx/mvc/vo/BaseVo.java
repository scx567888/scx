package cool.scx.mvc.vo;

import cool.scx.functional.ScxConsumer;
import cool.scx.standard.HttpHeader;
import cool.scx.standard.MediaType;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * BaseVo 接口
 * 所有需要向前台返回数据都需要继承
 *
 * @author scx567888
 * @version 0.5.0
 */
public interface BaseVo extends ScxConsumer<RoutingContext, Exception> {

    /**
     * 填充 ContentType a
     *
     * @param contentType a
     * @param response    a
     * @return a
     */
    static HttpServerResponse fillContentType(MediaType contentType, HttpServerResponse response) {
        if (contentType != null) {
            if (contentType.type().equals("text")) {
                return response.putHeader(HttpHeader.CONTENT_TYPE.toString(), contentType.toString(UTF_8));
            } else {
                return response.putHeader(HttpHeader.CONTENT_TYPE.toString(), contentType.toString());
            }
        } else {
            return response.putHeader(HttpHeader.CONTENT_TYPE.toString(), MediaType.APPLICATION_OCTET_STREAM.toString());
        }
    }

    /**
     * 填充 CONTENT_TYPE 为 json 格式
     *
     * @param response r
     * @return r
     */
    static HttpServerResponse fillJsonContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeader.CONTENT_TYPE.toString(), MediaType.APPLICATION_JSON.toString(UTF_8));
    }

    /**
     * 填充 CONTENT_TYPE 为 xml 格式
     *
     * @param response r
     * @return r
     */
    static HttpServerResponse fillXmlContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeader.CONTENT_TYPE.toString(), MediaType.APPLICATION_XML.toString(UTF_8));
    }

    /**
     * a
     *
     * @param response a
     * @return a
     */
    static HttpServerResponse fillHtmlContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeader.CONTENT_TYPE.toString(), MediaType.TEXT_HTML.toString(UTF_8));
    }

    /**
     * a
     *
     * @param response a
     * @return a
     */
    static HttpServerResponse fillTextPlainContentType(HttpServerResponse response) {
        return response.putHeader(HttpHeader.CONTENT_TYPE.toString(), MediaType.TEXT_PLAIN.toString(UTF_8));
    }

}
