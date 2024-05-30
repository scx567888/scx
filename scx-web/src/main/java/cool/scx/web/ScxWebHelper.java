package cool.scx.web;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.standard.HttpMethod;
import cool.scx.common.standard.MediaType;
import cool.scx.common.util.JsonNodeHelper;
import cool.scx.common.util.ObjectUtils;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.util.Map;

import static cool.scx.common.standard.HttpFieldName.CONTENT_TYPE;
import static cool.scx.common.standard.MediaType.*;
import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static io.vertx.core.http.HttpMethod.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ScxWebHelper {

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
        return jsonMapper(new ObjectUtils.Options().setIgnoreJsonIgnore(true)).readerFor(type).readValue(jsonNode);
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

    public static io.vertx.core.http.HttpMethod toVertxMethod(HttpMethod httpMethod) {
        return switch (httpMethod) {
            case CONNECT -> CONNECT;
            case DELETE -> DELETE;
            case GET -> GET;
            case HEAD -> HEAD;
            case OPTIONS -> OPTIONS;
            case PATCH -> PATCH;
            case POST -> POST;
            case PUT -> PUT;
            case TRACE -> TRACE;
        };
    }

    public static HttpServerResponse fillContentType(HttpServerResponse response, MediaType contentType) {
        if (contentType != null) {
            if (contentType.type().equals("text")) {
                return response.putHeader(CONTENT_TYPE.toString(), contentType.toString(UTF_8));
            } else {
                return response.putHeader(CONTENT_TYPE.toString(), contentType.toString());
            }
        } else {
            return response.putHeader(CONTENT_TYPE.toString(), APPLICATION_OCTET_STREAM.toString());
        }
    }

    public static HttpServerResponse fillJsonContentType(HttpServerResponse response) {
        return response.putHeader(CONTENT_TYPE.toString(), APPLICATION_JSON.toString(UTF_8));
    }

    public static HttpServerResponse fillXmlContentType(HttpServerResponse response) {
        return response.putHeader(CONTENT_TYPE.toString(), APPLICATION_XML.toString(UTF_8));
    }

    public static HttpServerResponse fillHtmlContentType(HttpServerResponse response) {
        return response.putHeader(CONTENT_TYPE.toString(), TEXT_HTML.toString(UTF_8));
    }

    public static HttpServerResponse fillTextPlainContentType(HttpServerResponse response) {
        return response.putHeader(CONTENT_TYPE.toString(), TEXT_PLAIN.toString(UTF_8));
    }

}
