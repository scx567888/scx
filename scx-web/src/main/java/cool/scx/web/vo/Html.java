package cool.scx.web.vo;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.routing.RoutingContext;

import java.io.IOException;
import java.nio.file.Path;

import static cool.scx.http.MediaType.TEXT_HTML;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * html 渲染类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class Html implements BaseVo {

    private final boolean usePath;
    private final Path htmlPath;
    private final String htmlStr;

    private Html(Path htmlPath, String htmlStr, boolean usePath) {
        this.htmlPath = htmlPath;
        this.htmlStr = htmlStr;
        this.usePath = usePath;
    }

    public static Html of(String htmlStr) {
        return new Html(null, htmlStr, false);
    }

    public static Html of(Path htmlPath) throws IOException {
        return new Html(htmlPath, null, true);
    }

    @Override
    public void accept(RoutingContext context) throws Exception {
        if (usePath) {
            sendHtmlPath(context);
        } else {
            sendHtmlStr(context);
        }
    }

    public void sendHtmlStr(RoutingContext context) {
        context.response()
                .contentType(ContentType.of(TEXT_HTML).charset(UTF_8))
                .send(htmlStr);
    }

    public void sendHtmlPath(RoutingContext context) {
        context.response()
                .contentType(ContentType.of(TEXT_HTML).charset(UTF_8))
                .send(htmlPath);
    }

}
