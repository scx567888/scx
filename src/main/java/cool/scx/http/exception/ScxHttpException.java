package cool.scx.http.exception;

import cool.scx.functional.ScxHandler;
import cool.scx.util.exception.ScxExceptionHelper;
import cool.scx.vo.VoHelper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.ext.web.RoutingContext;

import java.util.LinkedHashMap;

/**
 * 在 ScxMapping 注解标记的方法中抛出此异常会被ScxMappingHandler 进行截获并调用其中的 {@link #handle(RoutingContext)}}
 * <p>
 * 当我们的代码中有需要向客户端返回错误信息的时候
 * <p>
 * 推荐创建 HttpRequestException 的实现类并抛出异常 , 而不是手动进行异常的处理与响应的返回
 *
 * @author scx567888
 * @version 1.0.10
 */
public class ScxHttpException extends RuntimeException implements ScxHandler<RoutingContext> {

    /**
     * http 状态码
     */
    final int statusCode;

    /**
     * 简短说明
     */
    final String title;

    /**
     * 详细原因
     */
    final String info;

    /**
     * a
     *
     * @param statusCode a
     * @param title      a
     * @param info       a
     */
    public ScxHttpException(int statusCode, String title, String info) {
        this.statusCode = statusCode;
        this.title = title;
        this.info = info;
    }

    /**
     * a
     *
     * @param statusCode a
     * @param title      a
     */
    public ScxHttpException(int statusCode, String title) {
        this.statusCode = statusCode;
        this.title = title;
        this.info = "";
    }

    /**
     * a
     *
     * @param statusCode a
     * @param title      a
     * @param throwable  a
     */
    public ScxHttpException(int statusCode, String title, Throwable throwable) {
        this.statusCode = statusCode;
        this.title = title;
        this.info = ScxExceptionHelper.getCustomStackTrace(throwable);
    }

    /**
     * a
     *
     * @return a
     */
    public final int statusCode() {
        return this.statusCode;
    }

    /**
     * a
     *
     * @return a
     */
    public final String title() {
        return this.title;
    }

    /**
     * a
     *
     * @return a
     */
    public final String info() {
        return this.info;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        var accept = routingContext.request().headers().get(HttpHeaderNames.ACCEPT);
        //根据 accept 返回不同的错误信息
        if (accept != null && accept.toLowerCase().contains("text/html")) {
            VoHelper.fillHtmlContentType(routingContext.request().response().setStatusCode(statusCode)).end(toHtml());
        } else {
            VoHelper.fillJsonContentType(routingContext.request().response().setStatusCode(statusCode)).end(toJson());
        }
    }

    /**
     * a
     *
     * @return a
     */
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
        return String.format(htmlStr, title, statusCode, title, info);
    }

    /**
     * a
     *
     * @return a
     */
    public String toJson() {
        var tempMap = new LinkedHashMap<>();
        tempMap.put("statusCode", statusCode);
        tempMap.put("title", title);
        tempMap.put("info", info);
        return VoHelper.toJson(tempMap, "");
    }

}
