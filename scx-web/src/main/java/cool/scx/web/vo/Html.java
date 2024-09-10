package cool.scx.web.vo;

import cool.scx.http_server.ScxRoutingContext;

import java.io.IOException;
import java.nio.file.Path;

import static cool.scx.web.ScxWebHelper.fillHtmlContentType;

/**
 * html 渲染类
 *
 * @author scx567888
 * @version 0.3.6
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
    public void accept(ScxRoutingContext context) throws Exception {
        if (usePath) {
            sendHtmlPath(context);
        } else {
            sendHtmlStr(context);
        }
    }

    public void sendHtmlStr(ScxRoutingContext context) {
        fillHtmlContentType(context.request().response()).end(htmlStr);
    }

    public void sendHtmlPath(ScxRoutingContext context) {
        fillHtmlContentType(context.request().response()).sendFile(htmlPath.toFile());
    }

}
