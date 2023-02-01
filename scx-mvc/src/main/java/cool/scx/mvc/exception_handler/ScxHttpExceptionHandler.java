package cool.scx.mvc.exception_handler;

import cool.scx.mvc.exception.ScxHttpException;
import cool.scx.mvc.ScxHttpRouterExceptionHandler;
import cool.scx.util.HttpUtils;
import cool.scx.util.ObjectUtils;
import cool.scx.util.ScxExceptionHelper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

import static cool.scx.mvc.ScxMvcHelper.responseCanUse;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public class ScxHttpExceptionHandler implements ScxHttpRouterExceptionHandler {

    /**
     * 默认 html 模板
     */
    private static final String htmlTemplate = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>%s</title>
            </head>
            <body>
                <h1>%s - %s</h1>
                <hr>
                <pre>%s</pre>
            </body>
            </html>
            """;

    /**
     * a
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxHttpExceptionHandler.class);

    private final boolean useDevelopmentErrorPage;

    public ScxHttpExceptionHandler(boolean useDevelopmentErrorPage) {
        this.useDevelopmentErrorPage = useDevelopmentErrorPage;
    }

    /**
     * a
     *
     * @param routingContext a
     * @param statusCode     a
     * @param title          a
     * @param info           a
     */
    public static void sendToClient(int statusCode, String title, String info, RoutingContext routingContext) {
        //防止页面出现 null 这种奇怪的情况
        if (title == null) {
            title = "";
        }
        if (info == null) {
            info = "";
        }
        var accept = routingContext.request().headers().get(HttpHeaderNames.ACCEPT);
        //根据 accept 返回不同的错误信息
        if (accept != null && accept.toLowerCase().contains("text/html")) {
            var htmlStr = String.format(htmlTemplate, title, statusCode, title, info);
            HttpUtils.fillHtmlContentType(routingContext.request().response().setStatusCode(statusCode)).end(htmlStr);
        } else {
            var tempMap = new LinkedHashMap<>();
            tempMap.put("statusCode", statusCode);
            tempMap.put("title", title);
            tempMap.put("info", info);
            var jsonStr = ObjectUtils.toJson(tempMap, "");
            HttpUtils.fillJsonContentType(routingContext.request().response().setStatusCode(statusCode)).end(jsonStr);
        }
    }

    /**
     * a
     *
     * @param scxHttpException a
     * @param routingContext   a
     */
    public void handleScxHttpException(ScxHttpException scxHttpException, RoutingContext routingContext) {
        String info = null;
        //1, 这里根据是否开启了开发人员错误页面 进行相应的返回
        if (useDevelopmentErrorPage) {
            var cause = ScxExceptionHelper.getRootCause(scxHttpException.getCause());
            if (cause == null) {
                info = scxHttpException.getMessage();
            } else {
                info = ScxExceptionHelper.getStackTraceString(cause);
            }
        }
        sendToClient(scxHttpException.statusCode(), scxHttpException.title(), info, routingContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canHandle(Throwable throwable) {
        return throwable instanceof ScxHttpException;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Throwable throwable, RoutingContext context) {
        if (responseCanUse(context)) {
            //1, 这里根据是否开启了开发人员错误页面 进行相应的返回
            this.handleScxHttpException((ScxHttpException) throwable, context);
        } else {
            logger.error("捕获到 ScxHttpException 异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", throwable);
        }
    }

}
