package cool.scx.exception;

import cool.scx.vo.VoHelper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.ext.web.RoutingContext;

/**
 * a
 */
public final class ScxHttpExceptionHelper {

    /**
     * a
     *
     * @param code           a
     * @param title          a
     * @param info           a
     * @param routingContext a
     */
    public static void sendException(int code, String title, String info, RoutingContext routingContext) {
        var accept = routingContext.request().headers().get(HttpHeaderNames.ACCEPT);
        //根据 accept 返回不同的错误信息
        var scxHttpExceptionViewWrapper = new ScxHttpExceptionViewWrapper(code, title, info);
        if (accept != null && accept.toLowerCase().contains("text/html")) {
            VoHelper.fillHtmlContentType(routingContext.request().response().setStatusCode(code))
                    .end(toHtml(scxHttpExceptionViewWrapper));
        } else {
            VoHelper.fillJsonContentType(routingContext.request().response().setStatusCode(code))
                    .end(toJson(scxHttpExceptionViewWrapper));
        }
    }

    /**
     * a
     *
     * @param scxHttpExceptionViewWrapper a
     * @return a
     */
    public static String toJson(ScxHttpExceptionViewWrapper scxHttpExceptionViewWrapper) {
        return VoHelper.toJson(scxHttpExceptionViewWrapper, "");
    }

    /**
     * a
     *
     * @param scxHttpExceptionViewWrapper a
     * @return a
     */
    public static String toHtml(ScxHttpExceptionViewWrapper scxHttpExceptionViewWrapper) {
        var htmlStr = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Title</title>
                </head>
                <body>
                <h1>%s - %s</h1>
                <pre>%s</pre>
                </body>
                </html>
                """;
        return String.format(htmlStr, scxHttpExceptionViewWrapper.httpCode(), scxHttpExceptionViewWrapper.title(), scxHttpExceptionViewWrapper.info());
    }

}
