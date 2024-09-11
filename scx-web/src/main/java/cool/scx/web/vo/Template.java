package cool.scx.web.vo;

import cool.scx.http.ScxRoutingContext;
import cool.scx.web.template.ScxTemplateHandler;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.web.ScxWebHelper.fillHtmlContentType;

/**
 * 模板
 */
public final class Template {

    private final String templatePath;

    private final Map<String, Object> dataMap = new HashMap<>();

    private Template(String templatePath) {
        this.templatePath = templatePath;
    }

    public static Template of(String templatePath) throws IOException {
        return new Template(templatePath);
    }

    public Template add(String key, Object value) {
        dataMap.put(key, value);
        return this;
    }

    public void accept(ScxRoutingContext context, ScxTemplateHandler templateHandler) throws TemplateException, IOException {
        if (templateHandler == null) {
            throw new NullPointerException("handler 不能为空 !!!");
        }
        var response = fillHtmlContentType(context.request().response());
        var sw = new StringWriter();
        var template = templateHandler.getTemplate(templatePath);
        template.process(dataMap, sw);
        response.send(sw.toString());
    }

}
