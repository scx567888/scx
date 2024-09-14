package cool.scx.web.parameter_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import cool.scx.common.standard.MediaType;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.routing.RoutingContext;
import cool.scx.web.type.FormData;
import io.helidon.http.media.multipart.MultiPart;

import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static cool.scx.common.util.ObjectUtils.xmlMapper;
import static cool.scx.http.HttpFieldName.CONTENT_TYPE;
import static cool.scx.web.parameter_handler.RequestInfo.ContentType.*;

/**
 * 封装 RoutingContext 的参数 防止反复取值造成性能损失
 *
 * @author scx567888
 * @version 1.4.7
 */
public final class RequestInfo {

    private final RoutingContext routingContext;
    private final ContentType contentType;
    private JsonNode body;
    private FormData formData;

    public RequestInfo(RoutingContext ctx) {
        this.routingContext = ctx;
        this.contentType = initContentType(ctx);
        initBody(ctx, this.contentType);
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
        var contentType = ctx.request().getHeader(CONTENT_TYPE);
        if (contentType != null) {
            contentType = contentType.toLowerCase();
        }
        if (contentType == null) {
            return NULL;
        } else if (contentType.startsWith(MediaType.APPLICATION_JSON.toString())) {
            return APPLICATION_JSON;
        } else if (contentType.startsWith(MediaType.APPLICATION_XML.toString())) {
            return APPLICATION_XML;
        } else if (contentType.startsWith(MediaType.MULTIPART_FORM_DATA.toString())) {
            return MULTIPART_FORM_DATA;
        } else if (contentType.startsWith(MediaType.APPLICATION_X_WWW_FORM_URLENCODED.toString())) {
            return APPLICATION_X_WWW_FORM_URLENCODED;
        } else {
            return OTHER;
        }
    }

    /**
     * 根据不同的 ContentType 以不同的逻辑初始化 body
     *
     * @param ctx         ctx
     * @param contentType a
     */
    private void initBody(RoutingContext ctx, ContentType contentType) {
        switch (contentType) {
            case NULL -> {
                this.body = null;
                this.formData = null;
            }
            case APPLICATION_JSON -> {
                var string = ctx.request().body().asString();
                this.body = readJson(string);
            }
            case APPLICATION_XML -> {
                var string = ctx.request().body().asString();
                this.body = readXml(string);
            }
            case MULTIPART_FORM_DATA -> {
                var multiPart = ctx.request().body().as(MultiPart.class);
                this.formData = new FormData(multiPart);
            }
            default -> {
                var string = ctx.request().body().asString();
                this.body = string != null ? tryReadOrTextNode(string) : null;
            }
        }
    }

    public JsonNode body() {
        return body;
    }

    public FormData formData() {
        return formData;
    }

    public ContentType contentType() {
        return contentType;
    }

    public RoutingContext routingContext() {
        return routingContext;
    }

    public enum ContentType {
        APPLICATION_JSON,
        APPLICATION_XML,
        MULTIPART_FORM_DATA,
        APPLICATION_X_WWW_FORM_URLENCODED,
        OTHER,
        NULL
    }

}
