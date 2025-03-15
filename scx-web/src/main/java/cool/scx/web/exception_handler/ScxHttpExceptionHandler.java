package cool.scx.web.exception_handler;

import cool.scx.common.exception.ScxExceptionHelper;
import cool.scx.common.util.ObjectUtils;
import cool.scx.http.HttpStatusCode;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.header.content_type.ContentType;
import cool.scx.http.routing.RoutingContext;

import java.lang.System.Logger;
import java.util.LinkedHashMap;

import static cool.scx.common.util.StringUtils.startsWithIgnoreCase;
import static cool.scx.http.HttpFieldName.ACCEPT;
import static cool.scx.http.MediaType.APPLICATION_JSON;
import static cool.scx.http.MediaType.TEXT_HTML;
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

    public static void sendToClient(HttpStatusCode statusCode, String info, RoutingContext routingContext) {
        //防止页面出现 null 这种奇怪的情况
        if (info == null) {
            info = "";
        }
        var accept = routingContext.request().getHeader(ACCEPT);
        //根据 accept 返回不同的错误信息
        if (accept != null && startsWithIgnoreCase(accept, TEXT_HTML.value())) {
            var htmlStr = String.format(htmlTemplate, statusCode.description(), statusCode, statusCode.description(), info);
            routingContext.response()
                    .contentType(ContentType.of(TEXT_HTML).charset(UTF_8))
                    .status(statusCode).send(htmlStr);
        } else {
            var tempMap = new LinkedHashMap<>();
            tempMap.put("statusCode", statusCode);
            tempMap.put("title", statusCode.description());
            tempMap.put("info", info);
            var jsonStr = ObjectUtils.toJson(tempMap, "");
            routingContext.response()
                    .contentType(ContentType.of(APPLICATION_JSON).charset(UTF_8))
                    .status(statusCode)
                    .send(jsonStr);
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
        sendToClient(scxHttpException.statusCode(), info, routingContext);
    }

    @Override
    public boolean canHandle(Throwable throwable) {
        return throwable instanceof ScxHttpException;
    }

    @Override
    public void handle(Throwable throwable, RoutingContext routingContext) {
        if (!routingContext.response().isClosed()) {
            //1, 这里根据是否开启了开发人员错误页面 进行相应的返回
            this.handleScxHttpException((ScxHttpException) throwable, routingContext);
        } else {
            logger.log(ERROR, "捕获到 ScxHttpException 异常 !!!, 因为请求已被相应, 所以错误信息可能没有正确返回给客户端 !!!", throwable);
        }
    }

}
