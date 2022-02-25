package cool.scx.mvc.parameter_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import cool.scx.ScxContext;
import cool.scx.enumeration.ScxFeature;
import cool.scx.http.exception.impl.BadRequestException;
import cool.scx.type.UploadedEntity;
import cool.scx.util.ObjectUtils;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装 RoutingContext 的参数 防止反复取值造成性能损失
 *
 * @author scx567888
 * @version 1.4.7
 */
public final class ScxMappingRoutingContextInfo {

    private final RoutingContext routingContext;
    private final JsonNode body;
    private final Map<String, Object> queryParams;
    private final Map<String, UploadedEntity> uploadedEntityMap;

    /**
     * <p>Constructor for ScxMappingRequestParamInfo.</p>
     *
     * @param ctx a {@link io.vertx.ext.web.RoutingContext} object
     */
    public ScxMappingRoutingContextInfo(RoutingContext ctx) {
        this.routingContext = ctx;
        this.body = initBody(ctx);
        this.queryParams = multiMapToMap(ctx.queryParams());
        this.uploadedEntityMap = ctx.get("uploadedEntityMap") != null ? ctx.get("uploadedEntityMap") : new HashMap<>();
    }

    /**
     * 根据不同的 ContentType 以不同的逻辑初始化 body
     *
     * @param ctx ctx
     * @return c
     */
    private static JsonNode initBody(RoutingContext ctx) {
        var contentType = ctx.request().headers().get(HttpHeaderNames.CONTENT_TYPE);
        // contentType 不为空
        if (contentType != null) {
            contentType = contentType.toLowerCase();
            // json 类型的请求体
            if (contentType.startsWith(HttpHeaderValues.APPLICATION_JSON.toString())) {
                try {
                    return ObjectUtils.jsonMapper().readTree(ctx.getBodyAsString());
                } catch (JsonProcessingException e) {
                    if (ScxContext.getFeatureState(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE)) {
                        throw new BadRequestException(e);
                    } else {
                        throw new BadRequestException();
                    }
                }
            } else if (contentType.startsWith(HttpHeaderValues.MULTIPART_FORM_DATA.toString()) || contentType.startsWith(HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())) {
                // ContentType 为 multipart/form-data 或者 application/x-www-form-urlencoded 的时候 不需要从 body 中获取数据
                // 而是从 formAttributes 中获取
                if (ctx.request().formAttributes().isEmpty()) {
                    return NullNode.getInstance();
                } else {
                    return ObjectUtils.jsonMapper().valueToTree(multiMapToMap(ctx.request().formAttributes()));
                }
            } else if (contentType.startsWith(HttpHeaderValues.APPLICATION_XML.toString())) {
                //这里是 xml 的格式 (注意 : 如果 body 为空 则 xml 转换 也会失败 !!!)
                try {
                    return ObjectUtils.xmlMapper().readTree(ctx.getBodyAsString());
                } catch (JsonProcessingException e) {
                    if (ScxContext.getFeatureState(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE)) {
                        throw new BadRequestException(e);
                    } else {
                        throw new BadRequestException();
                    }
                }
            }
        }
        //走到这里标识以上的匹配全部失败 , 这里不知道 body 的具体格式 所以进行猜测转换
        var bodyAsString = ctx.getBodyAsString();
        //先尝试以 json 格式进行尝试转换
        try {
            return ObjectUtils.jsonMapper().readTree(bodyAsString);
        } catch (Exception exception) { //再尝试以 xml 的格式进行转换
            try {
                return ObjectUtils.xmlMapper().readTree(bodyAsString);
            } catch (JsonProcessingException e) {
                // json 和 xml 均转换失败 直接存储 为 string
                return new TextNode(bodyAsString);
            }
        }
    }

    /**
     * <p>multiMapToMap.</p>
     *
     * @param multiMap a {@link io.vertx.core.MultiMap} object
     * @return a {@link java.util.Map} object
     */
    private static Map<String, Object> multiMapToMap(MultiMap multiMap) {
        var map = new HashMap<String, Object>();
        for (var m : multiMap) {
            map.put(m.getKey(), m.getValue());
        }
        return map;
    }

    /**
     * a
     *
     * @return a
     */
    public JsonNode getBody() {
        return body;
    }

    /**
     * a
     *
     * @return a
     */
    public Map<String, UploadedEntity> uploadedEntityMap() {
        return uploadedEntityMap;
    }

    /**
     * a
     *
     * @return a
     */
    public Map<String, Object> queryParams() {
        return queryParams;
    }

    /**
     * a
     *
     * @return a
     */
    public RoutingContext routingContext() {
        return routingContext;
    }

}