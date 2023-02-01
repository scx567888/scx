package cool.scx.mvc.vo;

import cool.scx.mvc.return_value_handler.HtmlVoReturnValueHandler;
import freemarker.template.TemplateException;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * html 渲染类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class Html implements BaseVo {

    private final boolean useTemplate;

    private final String templatePath;

    private final String htmlStr;

    private final Map<String, Object> dataMap = new HashMap<>();

    private HtmlVoReturnValueHandler handler;

    /**
     * <p>Constructor for Html.</p>
     *
     * @param templatePath a {@link freemarker.template.Template} object
     * @param htmlStr      a {@link java.lang.String} object
     * @param useTemplate  a boolean
     */
    private Html(String templatePath, String htmlStr, boolean useTemplate) {
        this.templatePath = templatePath;
        this.htmlStr = htmlStr;
        this.useTemplate = useTemplate;
    }

    /**
     * <p>ofString.</p>
     *
     * @param htmlStr a {@link java.lang.String} object
     * @return a {@link Html} object
     */
    public static Html ofString(String htmlStr) {
        return new Html(null, htmlStr, false);
    }

    /**
     * <p>ofTemplate.</p>
     *
     * @param templatePath a {@link java.lang.String} object
     * @return a {@link Html} object
     * @throws java.io.IOException if any.
     */
    public static Html of(String templatePath) throws IOException {
        return new Html(templatePath, null, true);
    }

    /**
     * <p>add.</p>
     *
     * @param key   a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     * @return a {@link Html} object.
     */
    public Html add(String key, Object value) {
        dataMap.put(key, value);
        return this;
    }

    /**
     * {@inheritDoc}
     * <p>
     * sendToClient
     */
    @Override
    public void accept(RoutingContext context) throws TemplateException, IOException {
        if (useTemplate) {
            sendTemplate(context, this.handler);
        } else {
            sendHtmlStr(context);
        }
    }

    public void sendHtmlStr(RoutingContext context) {
        var response = BaseVo.fillHtmlContentType(context.request().response());
        response.end(htmlStr);
    }

    public void sendTemplate(RoutingContext context, HtmlVoReturnValueHandler handler) throws IOException, TemplateException {
        if (handler == null) {
            throw new NullPointerException("handler 不能为空 !!!");
        }
        var response = BaseVo.fillHtmlContentType(context.request().response());
        var sw = new StringWriter();
        var template = handler.getTemplateByPath(templatePath);
        template.process(dataMap, sw);
        response.end(sw.toString());
    }

    public Html setHandler(HtmlVoReturnValueHandler htmlVoReturnValueHandler) {
        this.handler = htmlVoReturnValueHandler;
        return this;
    }

}
