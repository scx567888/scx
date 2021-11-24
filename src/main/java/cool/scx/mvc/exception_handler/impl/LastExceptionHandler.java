package cool.scx.mvc.exception_handler.impl;

import cool.scx.ScxContext;
import cool.scx.enumeration.ScxFeature;
import cool.scx.mvc.ScxMappingHandler;
import cool.scx.mvc.exception_handler.ScxMappingExceptionHandler;
import cool.scx.util.ExceptionUtils;
import cool.scx.util.VoHelper;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LastExceptionHandler implements ScxMappingExceptionHandler {

    public static final LastExceptionHandler DEFAULT_INSTANCE = new LastExceptionHandler();
    private static final Logger logger = LoggerFactory.getLogger(ScxMappingHandler.class);

    @Override
    public boolean canHandle(Throwable throwable) {
        return true;
    }

    @Override
    public void handle(Throwable throwable, RoutingContext context) {
        //3, 打印错误信息
        logger.error("执行反射调用时发生异常 !!!", throwable);
        //4, 如果这时 response 还没有被关闭的话 就返回 500 错误信息
        if (!context.response().ended() && !context.response().closed()) {
            //5, 这里根据是否开启了开发人员错误页面 进行相应的返回
            VoHelper.fillTextPlainContentType(context.response().setStatusCode(500))
                    .end(ScxContext.getFeatureState(ScxFeature.USE_DEVELOPMENT_ERROR_PAGE) ?
                            ExceptionUtils.getCustomStackTrace(throwable) : "Internal Server Error !!!");
        }
    }
}
