package cool.scx.core.vo;

import cool.scx.core.ScxContext;
import freemarker.template.Template;
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

    private final Template template;

    private final String htmlStr;

    private final Map<String, Object> dataMap = new HashMap<>();

    /**
     * <p>Constructor for Html.</p>
     *
     * @param template    a {@link freemarker.template.Template} object
     * @param htmlStr     a {@link java.lang.String} object
     * @param useTemplate a boolean
     */
    private Html(Template template, String htmlStr, boolean useTemplate) {
        this.template = template;
        this.htmlStr = htmlStr;
        this.useTemplate = useTemplate;
    }

    /**
     * <p>ofString.</p>
     *
     * @param htmlStr a {@link java.lang.String} object
     * @return a {@link cool.scx.core.vo.Html} object
     */
    public static Html ofString(String htmlStr) {
        return new Html(null, htmlStr, false);
    }

    /**
     * <p>ofTemplate.</p>
     *
     * @param templatePath a {@link java.lang.String} object
     * @return a {@link cool.scx.core.vo.Html} object
     * @throws java.io.IOException if any.
     */
    public static Html of(String templatePath) throws IOException {
        var template = ScxContext.template().getTemplateByPath(templatePath);
        return new Html(template, null, true);
    }

    /**
     * <p>add.</p>
     *
     * @param key   a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     * @return a {@link cool.scx.core.vo.Html} object.
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
        var response = BaseVo.fillHtmlContentType(context.request().response());
        if (useTemplate) {
            var sw = new StringWriter();
            template.process(dataMap, sw);
            response.end(sw.toString());
        } else {
            response.end(htmlStr);
        }
    }

}
