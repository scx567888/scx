package cool.scx.web;

import cool.scx.common.standard.MediaType;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.routing.RoutingContext;

import static cool.scx.common.standard.MediaType.*;
import static cool.scx.http.HttpFieldName.CONTENT_TYPE;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ScxWebHelper {


    public static boolean responseCanUse(RoutingContext context) {
        return !context.request().response().isClosed();
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
