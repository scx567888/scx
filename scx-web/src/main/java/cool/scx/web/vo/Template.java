package cool.scx.web.vo;

import cool.scx.http.content_type.ContentType;
import cool.scx.http.routing.RoutingContext;
import cool.scx.web.template.ScxTemplateHandler;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static cool.scx.http.MediaType.TEXT_HTML;
import static java.nio.charset.StandardCharsets.UTF_8;

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

    public void accept(RoutingContext context, ScxTemplateHandler templateHandler) throws TemplateException, IOException {
        if (templateHandler == null) {
            throw new NullPointerException("handler 不能为空 !!!");
        }
        var sw = new StringWriter();
        var template = templateHandler.getTemplate(templatePath);
        template.process(dataMap, sw);
        context.response()
                .contentType(ContentType.of(TEXT_HTML).charset(UTF_8))
                .send(sw.toString());
    }

}
