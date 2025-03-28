package cool.scx.http.error_handler;

import cool.scx.common.exception.ScxExceptionHelper;
import cool.scx.common.util.ObjectUtils;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.exception.InternalServerErrorException;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.headers.accept.Accept;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.status.ScxHttpStatus;

import java.lang.System.Logger;
import java.util.Map;

import static cool.scx.http.error_handler.ErrorPhaseHelper.getErrorPhaseStr;
import static cool.scx.http.media_type.MediaType.APPLICATION_JSON;
import static cool.scx.http.media_type.MediaType.TEXT_HTML;
import static cool.scx.http.status.ScxHttpStatusHelper.getReasonPhrase;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.getLogger;
import static java.nio.charset.StandardCharsets.UTF_8;

/// 默认错误处理器
public class DefaultHttpServerErrorHandler implements ScxHttpServerErrorHandler {

    public static final DefaultHttpServerErrorHandler DEFAULT_HTTP_SERVER_ERROR_HANDLER = new DefaultHttpServerErrorHandler(true);

    private static final Logger LOGGER = getLogger(DefaultHttpServerErrorHandler.class.getName());

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

    public DefaultHttpServerErrorHandler(boolean useDevelopmentErrorPage) {
        this.useDevelopmentErrorPage = useDevelopmentErrorPage;
    }

    public static void sendToClient(ScxHttpStatus status, String info, ScxHttpServerRequest request) {
        //防止页面出现 null 这种奇怪的情况
        if (info == null) {
            info = "";
        }
        var reasonPhrase = getReasonPhrase(status, "unknown");
        Accept accepts = null;
        try {
            accepts = request.headers().accept();
        } catch (Exception _) {

        }
        //根据 accept 返回不同的错误信息 只有明确包含的时候才返回 html
        if (accepts != null && accepts.contains(TEXT_HTML)) {
            var htmlStr = String.format(htmlTemplate, reasonPhrase, status.code(), reasonPhrase, info);
            request.response()
                    .contentType(ScxMediaType.of(TEXT_HTML).charset(UTF_8))
                    .status(status)
                    .send(htmlStr);
        } else {
            var jsonStr = ObjectUtils.toJson(Map.of("status", status.code(), "title", reasonPhrase, "info", info), "");
            request.response()
                    .contentType(ScxMediaType.of(APPLICATION_JSON).charset(UTF_8))
                    .status(status)
                    .send(jsonStr);
        }
    }

    public void handleScxHttpException(ScxHttpException scxHttpException, ScxHttpServerRequest request) {
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
        sendToClient(scxHttpException.status(), info, request);
    }

    @Override
    public void accept(Throwable throwable, ScxHttpServerRequest request, ErrorPhase errorPhase) {
        var e = ScxExceptionHelper.getRootCause(throwable);
        // Http 异常无需打印
        if (e instanceof ScxHttpException h) {
            this.handleScxHttpException(h, request);
        } else {
            // 其余异常包装为 500 异常, 同时需要打印
            LOGGER.log(ERROR, getErrorPhaseStr(errorPhase) + " 发生异常 !!!", e);
            this.handleScxHttpException(new InternalServerErrorException(e), request);
        }
    }

}
