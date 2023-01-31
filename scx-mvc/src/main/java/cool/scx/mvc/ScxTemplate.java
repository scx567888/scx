package cool.scx.mvc;

import cool.scx.mvc.base.BaseTemplateDirective;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 用于渲染 freemarker
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class ScxTemplate {

    /**
     * Constant <code>logger</code>
     */
    private static final Logger logger = LoggerFactory.getLogger(ScxTemplate.class);

    /**
     * Freemarker 默认引擎版本
     */
    private static final Version VERSION = Configuration.VERSION_2_3_31;

    /**
     * Constant <code>freemarkerConfig</code>
     */
    private final Configuration freemarkerConfig = new Configuration(VERSION);


    public ScxTemplate(Path templateRoot) {
        // freemarker 配置文件版本
        var wrapperBuilder = new DefaultObjectWrapperBuilder(VERSION);
        //暴露 实体类的 fields 因为 此项目中的实体类没有 get set
        wrapperBuilder.setExposeFields(true);
        freemarkerConfig.setObjectWrapper(wrapperBuilder.build());
        try {
            freemarkerConfig.setDirectoryForTemplateLoading(templateRoot.toFile());
        } catch (Exception e) {
            logger.info("模板目录不存在!!! {}", templateRoot.toString());
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
            logger.debug("已添加自定义 Freemarker 标签 [{}] Class -> {}", myDirective.directiveName(), myDirective.getClass().getName());
            freemarkerConfig.setSharedVariable(myDirective.directiveName(), myDirective);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
