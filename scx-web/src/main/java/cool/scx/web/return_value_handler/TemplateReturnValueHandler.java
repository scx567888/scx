package cool.scx.web.return_value_handler;

import cool.scx.http.ScxRoutingContext;
import cool.scx.web.template.ScxTemplateHandler;
import cool.scx.web.vo.Template;

/**
 * 用于渲染 freemarker
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class TemplateReturnValueHandler implements ReturnValueHandler {

    private final ScxTemplateHandler templateHandler;

    public TemplateReturnValueHandler(ScxTemplateHandler templateHandler) {
        this.templateHandler = templateHandler;
    }

    @Override
    public boolean canHandle(Object returnValue) {
        return returnValue instanceof Template;
    }

    @Override
    public void handle(Object returnValue, ScxRoutingContext routingContext) throws Exception {
        if (returnValue instanceof Template template) {
            template.accept(routingContext, this.templateHandler);
        } else {
            throw new IllegalArgumentException("参数不是 Template 类型 !!! " + returnValue.getClass());
        }
    }

}
