package cool.scx.mvc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.util.JsonNodeHelper;
import cool.scx.util.ObjectUtils;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.util.Map;

import static cool.scx.util.ObjectUtils.jsonMapper;

public class ScxMvcHelper {

    /**
     * <p>getFromMap.</p>
     *
     * @param name     a {@link java.lang.String} object
     * @param map      a {@link io.vertx.core.MultiMap} object
     * @param useAll   a boolean
     * @param javaType a {@link com.fasterxml.jackson.databind.JavaType} object
     * @return a {@link java.lang.Object} object
     */
    public static Object getFromMap(String name, MultiMap map, boolean useAll, JavaType javaType) {
        if (useAll) {
            return map;
        } else if (javaType.isCollectionLikeType() || javaType.isArrayType()) {
            return map.getAll(name);
        } else {
            return map.get(name);
        }
    }

    /**
     * <p>getFromMap.</p>
     *
     * @param name   a {@link java.lang.String} object
     * @param map    a {@link java.util.Map} object
     * @param useAll a boolean
     * @return a {@link java.lang.Object} object
     */
    public static Object getFromMap(String name, Map<String, String> map, boolean useAll) {
        return useAll ? map : map.get(name);
    }

    /**
     * a
     *
     * @param jsonNode a
     * @param type     a
     * @param <T>      a
     * @return a
     * @throws java.io.IOException a
     */
    public static <T> T readValue(JsonNode jsonNode, JavaType type) throws IOException {
        return jsonMapper(ObjectUtils.Option.IGNORE_JSON_IGNORE).readerFor(type).readValue(jsonNode);
    }

    /**
     * <p>getFromJsonNode.</p>
     *
     * @param name     a {@link java.lang.String} object
     * @param jsonNode a {@link com.fasterxml.jackson.databind.JsonNode} object
     * @param useAll   a boolean
     * @return a {@link com.fasterxml.jackson.databind.JsonNode} object
     */
    public static JsonNode getFromJsonNode(String name, JsonNode jsonNode, boolean useAll) {
        return useAll ? jsonNode : JsonNodeHelper.get(jsonNode, name);
    }

    public static boolean responseCanUse(RoutingContext context) {
        return !context.request().response().ended() && !context.request().response().closed();
    }

}
