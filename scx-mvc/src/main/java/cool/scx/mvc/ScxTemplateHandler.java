package cool.scx.mvc;

import cool.scx.mvc.base.BaseTemplateDirective;
import cool.scx.mvc.return_value_handler.HtmlVoReturnValueHandler;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.Version;

import java.io.IOException;
import java.lang.System.Logger;
import java.nio.file.Path;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

/**
 * 模板处理器
 */
public final class ScxTemplateHandler {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = System.getLogger(HtmlVoReturnValueHandler.class.getName());

    /**
     * Freemarker 默认引擎版本
     */
    private static final Version VERSION = Configuration.VERSION_2_3_31;

    /**
     * Constant <code>freemarkerConfig</code>
     */
    private final Configuration freemarkerConfig = new Configuration(VERSION);

    public ScxTemplateHandler(Path templateRoot) {
        // freemarker 配置文件版本
        var wrapperBuilder = new DefaultObjectWrapperBuilder(VERSION);
        //暴露 实体类的 fields 因为 此项目中的实体类没有 get set
        wrapperBuilder.setExposeFields(true);
        freemarkerConfig.setObjectWrapper(wrapperBuilder.build());
        try {
            freemarkerConfig.setDirectoryForTemplateLoading(templateRoot.toFile());
        } catch (Exception e) {
            logger.log(INFO, "模板目录不存在!!! {0}", templateRoot);
        }

        //设置 字符集
        freemarkerConfig.setDefaultEncoding("UTF-8");
        //设置 语法 为自动检测
        freemarkerConfig.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
    }

    /**
     * <p>getTemplateByPath.</p>
     *
     * @param pagePath a {@link java.lang.String} object.
     * @return a {@link freemarker.template.Template} object.
     * @throws java.io.IOException if any.
     */
    public Template getTemplateByPath(String pagePath) throws IOException {
        return freemarkerConfig.getTemplate(pagePath + ".html");
    }

    public void addDirective(BaseTemplateDirective myDirective) {
        try {
            logger.log(DEBUG, "已添加自定义 Freemarker 标签 [{0}] Class -> {1}", myDirective.directiveName(), myDirective.getClass().getName());
            freemarkerConfig.setSharedVariable(myDirective.directiveName(), myDirective);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
