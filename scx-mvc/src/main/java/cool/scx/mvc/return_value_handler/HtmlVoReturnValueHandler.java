package cool.scx.mvc.return_value_handler;

import cool.scx.mvc.ScxMvcReturnValueHandler;
import cool.scx.mvc.ScxTemplateHandler;
import cool.scx.mvc.vo.Html;
import io.vertx.ext.web.RoutingContext;

/**
 * 用于渲染 freemarker
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class HtmlVoReturnValueHandler implements ScxMvcReturnValueHandler {

    private final ScxTemplateHandler templateHandler;

    public HtmlVoReturnValueHandler(ScxTemplateHandler templateHandler) {
        this.templateHandler = templateHandler;
    }

    @Override
    public boolean canHandle(Object result) {
        return result instanceof Html;
    }

    @Override
    public void handle(Object result, RoutingContext context) throws Exception {
        Html html = (Html) result;
        html.accept(context, this.templateHandler);
    }

}
