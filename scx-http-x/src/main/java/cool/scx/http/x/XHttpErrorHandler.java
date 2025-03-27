package cool.scx.http.x;

import cool.scx.common.exception.ScxExceptionHelper;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.exception.InternalServerErrorException;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.http.media_type.ScxMediaType;
import cool.scx.http.status.ScxHttpStatus;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

import static cool.scx.http.media_type.MediaType.TEXT_HTML;
import static cool.scx.http.status.ScxHttpStatusHelper.getReasonPhrase;
import static java.nio.charset.StandardCharsets.UTF_8;

public class XHttpErrorHandler implements BiConsumer<Throwable, ScxHttpServerRequest> {

    public static final XHttpErrorHandler X_HTTP_ERROR_HANDLER = new XHttpErrorHandler(true);

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

    public XHttpErrorHandler(boolean useDevelopmentErrorPage) {
        this.useDevelopmentErrorPage = useDevelopmentErrorPage;
    }

    public static void sendToClient(ScxHttpStatus status, String info, ScxHttpServerRequest request) {
        //防止页面出现 null 这种奇怪的情况
        if (info == null) {
            info = "";
        }
        var reasonPhrase = getReasonPhrase(status, "unknown");
        var accepts = request.headers().accept();
        //根据 accept 返回不同的错误信息 只有明确包含的时候才返回 html
        if (accepts != null && accepts.contains(TEXT_HTML)) {
            var htmlStr = String.format(htmlTemplate, reasonPhrase, status.code(), reasonPhrase, info);
            request.response()
                    .contentType(ScxMediaType.of(TEXT_HTML).charset(UTF_8))
                    .status(status)
                    .send(htmlStr);
        } else {
            var tempMap = new LinkedHashMap<>();
            tempMap.put("status", status);
            tempMap.put("title", reasonPhrase);
            tempMap.put("info", info);
            request.response()
                    .status(status)
                    .send(tempMap);
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
    public void accept(Throwable throwable, ScxHttpServerRequest request) {
        var httpException = throwable instanceof ScxHttpException h ? h : new InternalServerErrorException(throwable);
        //1, 这里根据是否开启了开发人员错误页面 进行相应的返回
        this.handleScxHttpException(httpException, request);
    }

}
