package cool.scx.web;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.common.standard.MediaType;
import cool.scx.common.util.JsonNodeHelper;
import cool.scx.common.util.ObjectUtils;
import cool.scx.http.HttpMethod;
import cool.scx.http.Parameters;
import cool.scx.http.URIQuery;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.routing.RoutingContext;
import cool.scx.web.type.FormData;
import io.helidon.common.uri.UriQuery;

import java.io.IOException;
import java.util.Map;

import static cool.scx.common.standard.MediaType.*;
import static cool.scx.common.util.ObjectUtils.jsonMapper;
import static cool.scx.http.HttpFieldName.CONTENT_TYPE;
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
    public static Object getFromMap(String name, URIQuery map, boolean useAll, JavaType javaType) {
        if (useAll) {
            return map;
        } else if (javaType.isCollectionLikeType() || javaType.isArrayType()) {
            return map.getAll(name);
        } else {
            return map.get(name);
        }
    }


    public static Object getFromMap(String name, FormData formData, boolean useAllBody, JavaType javaType) {
        return null;
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


    public static Object getFromMap(String name, Parameters parameters, boolean merge) {
        return null;
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
        return !context.request().response().closed();
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

    public static ScxHttpServerResponse fillContentType(ScxHttpServerResponse response, MediaType contentType) {
        if (contentType != null) {
            if (contentType.type().equals("text")) {
                return response.setHeader(CONTENT_TYPE, contentType.toString(UTF_8));
            } else {
                return response.setHeader(CONTENT_TYPE, contentType.toString());
            }
        } else {
            return response.setHeader(CONTENT_TYPE, APPLICATION_OCTET_STREAM.toString());
        }
    }

    public static ScxHttpServerResponse fillJsonContentType(ScxHttpServerResponse response) {
        return response.setHeader(CONTENT_TYPE, APPLICATION_JSON.toString(UTF_8));
    }

    public static ScxHttpServerResponse fillXmlContentType(ScxHttpServerResponse response) {
        return response.setHeader(CONTENT_TYPE, APPLICATION_XML.toString(UTF_8));
    }

    public static ScxHttpServerResponse fillHtmlContentType(ScxHttpServerResponse response) {
        return response.setHeader(CONTENT_TYPE, TEXT_HTML.toString(UTF_8));
    }

    public static ScxHttpServerResponse fillTextPlainContentType(ScxHttpServerResponse response) {
        return response.setHeader(CONTENT_TYPE, TEXT_PLAIN.toString(UTF_8));
    }

}
