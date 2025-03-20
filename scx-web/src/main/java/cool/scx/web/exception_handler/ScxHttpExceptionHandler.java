package cool.scx.web.exception_handler;

import cool.scx.common.exception.ScxExceptionHelper;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.routing.RoutingContext;
import cool.scx.http.status.ScxHttpStatus;

import java.lang.System.Logger;
import java.util.LinkedHashMap;

import static cool.scx.http.media_type.MediaType.TEXT_HTML;
import static cool.scx.http.status.ScxHttpStatusHelper.getReasonPhrase;
import static java.lang.System.Logger.Level.ERROR;
import static java.nio.charset.StandardCharsets.UTF_8;

/// ScxHttpException 处理器
///
/// @author scx567888
/// @version 0.0.1
public class ScxHttpExceptionHandler implements ExceptionHandler {

    private static final Logger logger = System.getLogger(ScxHttpExceptionHandler.class.getName());

    /// 默认 html 模板
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

    private final boolean useDevelopmentErrorPage;

    public ScxHttpExceptionHandler(boolean useDevelopmentErrorPage) {
        this.useDevelopmentErrorPage = useDevelopmentErrorPage;
    }

    public static void sendToClient(ScxHttpStatus status, String info, RoutingContext routingContext) {
        //防止页面出现 null 这种奇怪的情况
        if (info == null) {
            info = "";
        }
        var reasonPhrase = getReasonPhrase(status, "unknown");
        var accepts = routingContext.request().headers().accept();
        //根据 accept 返回不同的错误信息 只有明确包含的时候才返回 html
        if (accepts != null && accepts.contains(TEXT_HTML)) {
            var htmlStr = String.format(htmlTemplate, reasonPhrase, status, reasonPhrase, info);
            routingContext.response()
                    .contentType(ScxMediaType.of(TEXT_HTML).charset(UTF_8))
                    .status(status)
                    .send(htmlStr);
        } else {
            var tempMap = new LinkedHashMap<>();
            tempMap.put("status", status);
            tempMap.put("title", reasonPhrase);
            tempMap.put("info", info);
            routingContext.response()
                    .status(status)
                    .send(tempMap);
        }
    }

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
        sendToClient(scxHttpException.status(), info, routingContext);
    }

    @Override
    public boolean canHandle(Throwable throwable) {
        return throwable instanceof ScxHttpException;
    }

    @Override
    public void handle(Throwable throwable, RoutingContext routingContext) {
        if (!routingContext.response().isSent()) {
            //1, 这里根据是否开启了开发人员错误页面 进行相应的返回
            this.handleScxHttpException((ScxHttpException) throwable, routingContext);
        } else {
            logger.log(ERROR, "捕获到 ScxHttpException 异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", throwable);
        }
    }

}
