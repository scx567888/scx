package cool.scx;

import cool.scx.base.BaseTemplateDirective;
import cool.scx.config.ScxEasyConfig;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

    /**
     * <p>Constructor for ScxTemplate.</p>
     *
     * @param scxEasyConfig a
     */
    public ScxTemplate(ScxEasyConfig scxEasyConfig) {
        // freemarker 配置文件版本
        var wrapperBuilder = new DefaultObjectWrapperBuilder(VERSION);
        //暴露 实体类的 fields 因为 此项目中的实体类没有 get set
        wrapperBuilder.setExposeFields(true);
        freemarkerConfig.setObjectWrapper(wrapperBuilder.build());
        try {
            freemarkerConfig.setDirectoryForTemplateLoading(scxEasyConfig.templateRoot());
        } catch (Exception e) {
            logger.error("模板目录不存在!!! {}", scxEasyConfig.templateRoot().getPath());
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

    /**
     * 添加一个模板指令
     *
     * @param clazz a {@link java.lang.Class} object
     */
    public void addDirective(Class<? extends BaseTemplateDirective> clazz) {
        try {
            var myDirective = ScxContext.getBean(clazz);
            logger.debug("已添加自定义 Freemarker 标签 [{}] Class -> {}", myDirective.directiveName(), clazz.getName());
            freemarkerConfig.setSharedVariable(myDirective.directiveName(), myDirective);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
