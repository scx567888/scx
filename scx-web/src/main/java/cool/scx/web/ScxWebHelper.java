package cool.scx.web;

import cool.scx.http.MediaType;
import cool.scx.http.ScxHttpServerResponse;
import cool.scx.http.content_type.ContentType;
import cool.scx.http.routing.RoutingContext;

import static cool.scx.http.MediaType.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ScxWebHelper {


    public static boolean responseCanUse(RoutingContext context) {
        return !context.request().response().isClosed();
    }

    public static ScxHttpServerResponse fillContentType(ScxHttpServerResponse response, MediaType contentType) {
        if (contentType != null) {
            if (contentType.type().equals("text")) {
                return response.contentType(ContentType.of(contentType).charset(UTF_8));
            } else {
                return response.contentType(ContentType.of(contentType));
            }
        } else {
            return response.contentType(ContentType.of(APPLICATION_OCTET_STREAM));
        }
    }

    public static ScxHttpServerResponse fillJsonContentType(ScxHttpServerResponse response) {
        return response.contentType(ContentType.of(APPLICATION_JSON).charset(UTF_8));
    }

    public static ScxHttpServerResponse fillXmlContentType(ScxHttpServerResponse response) {
        return response.contentType( ContentType.of(APPLICATION_XML).charset(UTF_8));
    }

    public static ScxHttpServerResponse fillHtmlContentType(ScxHttpServerResponse response) {
        return response.contentType(ContentType.of(TEXT_HTML).charset(UTF_8) );
    }

    public static ScxHttpServerResponse fillTextPlainContentType(ScxHttpServerResponse response) {
        return response.contentType(ContentType.of(TEXT_PLAIN).charset(UTF_8));
    }

}
