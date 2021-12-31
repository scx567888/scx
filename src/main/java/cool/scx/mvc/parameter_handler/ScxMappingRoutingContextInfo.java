package cool.scx.mvc.parameter_handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.bo.UploadedEntity;
import cool.scx.util.ObjectUtils;
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
    private final JsonNode jsonBody;
    private final Map<String, Object> formAttributesMap;
    private final Map<String, Object> queryParams;
    private final Map<String, UploadedEntity> uploadedEntityMap;
    private final boolean isJsonBody;//是不是 json格式的请求

    /**
     * <p>Constructor for ScxMappingRequestParamInfo.</p>
     *
     * @param ctx a {@link io.vertx.ext.web.RoutingContext} object
     */
    public ScxMappingRoutingContextInfo(RoutingContext ctx) {
        this.routingContext = ctx;
        this.jsonBody = initJsonBody(ctx);
        this.formAttributesMap = multiMapToMap(ctx.request().formAttributes());
        this.isJsonBody = this.formAttributesMap.size() == 0;
        this.queryParams = multiMapToMap(ctx.queryParams());
        this.uploadedEntityMap = ctx.get("uploadedEntityMap") != null ? ctx.get("uploadedEntityMap") : new HashMap<>();
    }

    private static JsonNode initJsonBody(RoutingContext ctx) {
        //先从多个来源获取参数 并缓存起来
        try {
            return ObjectUtils.jsonMapper().readTree(ctx.getBodyAsString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>multiMapToMap.</p>
     *
     * @param multiMap a {@link io.vertx.core.MultiMap} object
     * @return a {@link java.util.Map} object
     */
    public static Map<String, Object> multiMapToMap(MultiMap multiMap) {
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
    public boolean isJsonBody() {
        return isJsonBody;
    }

    /**
     * a
     *
     * @return a
     */
    public Map<String, Object> formAttributesMap() {
        return formAttributesMap;
    }

    /**
     * a
     *
     * @return a
     */
    public JsonNode getJsonBody() {
        return jsonBody;
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