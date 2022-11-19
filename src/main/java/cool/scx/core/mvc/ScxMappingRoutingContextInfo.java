package cool.scx.core.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import cool.scx.core.http.exception.BadRequestException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.ext.web.RoutingContext;

import static cool.scx.core.mvc.ScxMappingRoutingContextInfo.ContentType.*;
import static cool.scx.util.ObjectUtils.jsonMapper;
import static cool.scx.util.ObjectUtils.xmlMapper;

/**
 * 封装 RoutingContext 的参数 防止反复取值造成性能损失
 *
 * @author scx567888
 * @version 1.4.7
 */
public final class ScxMappingRoutingContextInfo {

    private final RoutingContext routingContext;
    private final JsonNode body;
    private final ContentType contentType;

    /**
     * <p>Constructor for ScxMappingRequestParamInfo.</p>
     *
     * @param ctx a {@link io.vertx.ext.web.RoutingContext} object
     */
    public ScxMappingRoutingContextInfo(RoutingContext ctx) {
        this.routingContext = ctx;
        this.contentType = initContentType(ctx);
        this.body = initBody(ctx, this.contentType);
    }

    /**
     * 根据不同的 ContentType 以不同的逻辑初始化 body
     *
     * @param ctx         ctx
     * @param contentType a
     * @return c a
     */
    public static JsonNode initBody(RoutingContext ctx, ContentType contentType) {
        var bodyAsString = ctx.body().asString();
        if (bodyAsString == null) {
            return null;
        }
        return switch (contentType) {
            case JSON -> readJson(bodyAsString);
            case XML -> readXml(bodyAsString);
            default -> tryReadOrTextNode(bodyAsString);
        };
    }

    public static JsonNode readJson(String jsonStr) {
        try {
            return jsonMapper().readTree(jsonStr);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e);
        }
    }

    public static JsonNode readXml(String xmlStr) {
        try {
            return xmlMapper().readTree(xmlStr);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(e);
        }
    }

    /**
     * 走到这里标识以上的匹配全部失败 , 这里不知道 body 的具体格式 所以进行猜测转换
     *
     * @param str a
     * @return a
     */
    public static JsonNode tryReadOrTextNode(String str) {
        try { //先尝试以 json 格式进行尝试转换
            return jsonMapper().readTree(str);
        } catch (Exception exception) {
            try {//再尝试以 xml 的格式进行转换
                return xmlMapper().readTree(str);
            } catch (JsonProcessingException e) {
                // json 和 xml 均转换失败 直接存储 为 string
                return new TextNode(str);
            }
        }
    }

    public static ContentType initContentType(RoutingContext ctx) {
        var contentType = ctx.request().headers().get(HttpHeaderNames.CONTENT_TYPE);
        if (contentType != null) {
            contentType = contentType.toLowerCase();
        }
        if (contentType == null) {
            return NULL;
        } else if (contentType.startsWith(HttpHeaderValues.APPLICATION_JSON.toString())) {
            return JSON;
        } else if (contentType.startsWith(HttpHeaderValues.APPLICATION_XML.toString())) {
            return XML;
        } else if (contentType.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString())
                || contentType.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())) {
            return FORM;
        } else {
            return OTHER;
        }
    }

    /**
     * a
     *
     * @return a
     */
    public JsonNode body() {
        return body;
    }

    public ContentType contentType() {
        return contentType;
    }

    /**
     * a
     *
     * @return a
     */
    public RoutingContext routingContext() {
        return routingContext;
    }

    public enum ContentType {
        FORM,
        JSON,
        XML,
        OTHER,
        NULL
    }

}