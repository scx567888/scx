package cool.scx.exception;

import cool.scx.ScxHandler;
import cool.scx.vo.VoHelper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.ext.web.RoutingContext;

/**
 * 在 ScxMapping 注解标记的方法中抛出此异常会被ScxMappingHandler 进行截获并调用其中的 {@link #handle(Object)}
 * <p>
 * 当我们的代码中有需要向客户端返回错误信息的时候
 * <p>
 * 推荐创建 HttpRequestException 的实现类并抛出异常 , 而不是手动进行异常的处理与响应的返回
 *
 * @author scx567888
 * @version 1.0.10
 */
public class ScxHttpException extends RuntimeException implements ScxHandler<RoutingContext> {

    final int httpStatusCode;
    final String title;
    final String info;

    public ScxHttpException(int httpStatusCode, String title, String info) {
        this.httpStatusCode = httpStatusCode;
        this.title = title;
        this.info = info;
    }

    public final int httpStatusCode() {
        return this.httpStatusCode;
    }

    public final String title() {
        return this.title;
    }

    public final String info() {
        return this.info;
    }

    public void sendException(RoutingContext routingContext) {
        var accept = routingContext.request().headers().get(HttpHeaderNames.ACCEPT);
        //根据 accept 返回不同的错误信息
        var scxHttpExceptionViewWrapper = new ScxHttpExceptionViewWrapper(httpStatusCode, title, info);
        if (accept != null && accept.toLowerCase().contains("text/html")) {
            VoHelper.fillHtmlContentType(routingContext.request().response().setStatusCode(httpStatusCode))
                    .end(scxHttpExceptionViewWrapper.toHtml());
        } else {
            VoHelper.fillJsonContentType(routingContext.request().response().setStatusCode(httpStatusCode))
                    .end(scxHttpExceptionViewWrapper.toJson());
        }
    }

    @Override
    public void handle(RoutingContext routingContext) {
        routingContext.fail(httpStatusCode(), this);
    }

    private record ScxHttpExceptionViewWrapper(int httpStatusCode, String title, String info) {

        public String toHtml() {
            var htmlStr = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <title>%s</title>
                    </head>
                    <body>
                        <h1>%s - %s</h1>
                        <pre>%s</pre>
                    </body>
                    </html>
                    """;
            return String.format(htmlStr, title, httpStatusCode, title, info);
        }

        public String toJson() {
            return VoHelper.toJson(this, "");
        }

    }

}
