package cool.scx.core.mvc;

import cool.scx.core.Scx;
import cool.scx.core.annotation.ScxMapping;
import cool.scx.core.http.ScxHttpRouter;
import io.vertx.ext.web.Router;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ScxMappingRegistrar {

    private final List<ScxMappingHandler> scxMappingHandlers;

    /**
     * 扫描所有被 ScxMapping注解标记的方法 并封装为 ScxMappingHandler.
     *
     * @param scx           s
     * @param scxHttpRouter s
     */
    public ScxMappingRegistrar(Scx scx, ScxHttpRouter scxHttpRouter) {
        scxMappingHandlers = Arrays.stream(scx.scxModules())
                .flatMap(c -> c.scxMappingClassList().stream())
                .flatMap(c -> Arrays.stream(c.getMethods())
                        .filter(m -> m.isAnnotationPresent(ScxMapping.class))
                        .map(m -> new ScxMappingHandler(c, m, scx, scxHttpRouter)))
                .toList();
    }

    /**
     * 获取所有被ScxMapping注解标记的方法的 handler
     *
     * @return 所有 handler
     */
    public List<ScxMappingHandler> scxMappingHandlers() {
        return scxMappingHandlers;
    }

    /**
     * 校验路由是否已经存在
     *
     * @param handler h
     * @return true 为存在 false 为不存在
     */
    private boolean checkScxMappingHandlerRouteExists(ScxMappingHandler handler) {
//        for (var a : SCX_MAPPING_HANDLER_LIST) {
//            if (!a.patternUrl.equals(handler.patternUrl)) {
//                continue;
//            }
//            for (var h : handler.httpMethods) {
//                if (Arrays.stream(a.httpMethods).toList().contains(h)) {
//                    logger.error("检测到重复的路由!!! {} --> \"{}\" , 相关 class 及方法如下 ▼" + System.lineSeparator() + "\t{} : {}" + System.lineSeparator() + "\t{} : {}", h, handler.patternUrl, handler.clazz.getName(), handler.method.getName(), a.clazz.getName(), a.method.getName());
//                    return true;
//                }
//            }
//        }
        return false;
    }

    public void registerRoute(Router router) {
        //todo 在注册路由前我们要进行一个排序 规则如下
        //1 若注解上标识了 order 则按照注解上的 order 进行插入
        //2 若注解上的 order 为 -1 (默认) 则针对路由精确度进行匹配
        //  如 /api/user/list > /api/user/:m > /api/:u/:m/ (按照参数数量倒序排序)
        // todo 排序规则此处需要修改
        //todo 校验路由是否已经存在 ? 规则 ?
//        if (!checkScxMappingHandlerRouteExists(s)) {

//        }
        var sortedList = scxMappingHandlers.stream()
                .sorted(Comparator.comparing(ScxMappingHandler::order)).toList();


        //循环添加到 vertxRouter 中
        for (var c : sortedList) {
            var r = router.route(c.url);
            for (var httpMethod : c.httpMethods) {
                r.method(httpMethod.vertxMethod());
            }
            r.handler(c);
        }

    }

}
