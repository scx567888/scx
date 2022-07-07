package cool.scx.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.scx.ScxHandlerE;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static cool.scx.vo.VoHelper.JSON_MAPPER;
import static cool.scx.vo.VoHelper.XML_MAPPER;

/**
 * BaseVo 接口
 * 所有需要向前台返回数据都需要继承
 *
 * @author scx567888
 * @version 0.5.0
 */
public interface BaseVo extends ScxHandlerE<RoutingContext, Exception> {

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
     * 将 一个对象转换为 json 字符串 (这里本质上是调用了)
     * <p>
     * 所以其中标注的所有注解均会执行 , 包括 @JsonIgnore
     * 也就是说如果想向前台发送一些 json 建议使用此方法进行序列化
     * 以保证标注 @JsonIgnore 注解的属性可以被忽略 防止一些私密信息泄露
     *
     * @param value a
     * @return a
     * @throws com.fasterxml.jackson.core.JsonProcessingException a
     */
    static String toJson(Object value) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(value);
    }

    /**
     * a
     *
     * @param value        a
     * @param defaultValue a
     * @return a
     */
    static String toJson(Object value, String defaultValue) {
        try {
            return toJson(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * a
     *
     * @param value a
     * @return a
     * @throws com.fasterxml.jackson.core.JsonProcessingException a
     */
    static String toXml(Object value) throws JsonProcessingException {
        return XML_MAPPER.writeValueAsString(value);
    }

    /**
     * a
     *
     * @param value        a
     * @param defaultValue a
     * @return a
     */
    static String toXml(Object value, String defaultValue) {
        try {
            return toXml(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
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
