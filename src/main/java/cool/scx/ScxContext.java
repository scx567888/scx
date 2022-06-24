package cool.scx;

import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEasyConfig;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.dao.ScxDao;
import cool.scx.enumeration.ScxFeature;
import cool.scx.eventbus.ScxEventBus;
import cool.scx.http.ScxHttpRouter;
import cool.scx.mvc.ScxMappingConfiguration;
import cool.scx.scheduler.ScxScheduler;
import cool.scx.sql.SQLRunner;
import cool.scx.websocket.ScxWebSocketRouter;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.RoutingContext;

import java.nio.file.Path;
import java.util.List;

/**
 * 用来存储 整个项目的上下文
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxContext {

    /**
     * 路由上下文 THREAD_LOCAL
     */
    private static final InheritableThreadLocal<RoutingContext> ROUTING_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 全局唯一的 SCX APP
     * <br>
     * 为了保证方法使用的简易 我们建议使用静态的方法
     * 但是其本质上是调用 GLOBAL_UNIQUE_SCX_APP 方法中的实例对象
     */
    private static Scx GLOBAL_SCX = null;

    /**
     * 获取当前线程的 RoutingContext (只限在 scx mapping 注解的方法及其调用链上)
     *
     * @return 当前线程的 RoutingContext
     */
    public static RoutingContext routingContext() {
        return ROUTING_CONTEXT_THREAD_LOCAL.get();
    }

    /**
     * 设置当前线程的 routingContext
     * 此方法正常之给 scxMappingHandler 调用
     * 若无特殊需求 不必调用此方法
     *
     * @param routingContext 要设置的 routingContext
     */
    public static void _routingContext(RoutingContext routingContext) {
        ROUTING_CONTEXT_THREAD_LOCAL.set(routingContext);
    }

    /**
     * a
     */
    public static void _clearRoutingContext() {
        ROUTING_CONTEXT_THREAD_LOCAL.remove();
    }

    /**
     * 设置全局的 Scx
     *
     * @param scx scx
     */
    static void scx(Scx scx) {
        GLOBAL_SCX = scx;
    }

    /**
     * 获取全局的 Scx
     *
     * @return scx
     */
    public static Scx scx() {
        if (GLOBAL_SCX != null) {
            return GLOBAL_SCX;
        } else {
            throw new RuntimeException("全局 Scx 未初始化 !!! 请先使用 Scx.builder() 创建 Scx 实例 , 全局 Scx 会自动设置 !!!");
        }
    }

    /**
     * a
     *
     * @return a
     */
    public static ScxFeatureConfig featureConfig() {
        return scx().scxFeatureConfig();
    }

    /**
     * a
     *
     * @return a
     */
    public static ScxConfig config() {
        return scx().scxConfig();
    }

    /**
     * 获取 dao
     *
     * @return d
     */
    public static ScxDao dao() {
        return scx().scxDao();
    }

    /**
     * a
     *
     * @return a
     */
    public static HttpServer httpServer() {
        return scx().vertxHttpServer();
    }

    /**
     * a
     *
     * @return a
     */
    public static ScxEventBus eventBus() {
        return scx().scxEventBus();
    }

    /**
     * a
     *
     * @return a
     */
    public static ScxWebSocketRouter webSocketRouter() {
        return scx().scxWebSocketRouter();
    }

    /**
     * <p>appRoot.</p>
     *
     * @return a {@link java.io.File} object
     */
    public static ScxEnvironment environment() {
        return scx().scxEnvironment();
    }

    /**
     * <p>appKey.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public static String appKey() {
        return scx().appKey();
    }

    /**
     * 所有模块
     *
     * @return a {@link java.util.List} object.
     */
    public static List<ScxModuleMetadata<?>> scxModuleMetadataList() {
        return scx().scxModuleMetadataList();
    }

    /**
     * a
     *
     * @return a
     */
    public static ScxEasyConfig easyConfig() {
        return scx().scxEasyConfig();
    }

    /**
     * 返回当前运行的 scx 实例的 beanFactory
     *
     * @return a
     */
    public static ScxBeanFactory beanFactory() {
        return scx().scxBeanFactory();
    }

    /**
     * a
     *
     * @return a
     */
    public static ScxHttpRouter router() {
        return scx().scxHttpRouter();
    }

    /**
     * 返回当前运行的 scx 实例的 template
     *
     * @return a
     */
    public static ScxTemplate template() {
        return scx().scxTemplate();
    }

    /**
     * 返回当前运行的 scx 实例的 vertx
     *
     * @return 全局的事件总线
     */
    public static Vertx vertx() {
        return scx().vertx();
    }

    /**
     * 返回当前运行的 scx 实例的 scheduler
     *
     * @return a
     */
    public static ScxScheduler scheduler() {
        return scx().scxScheduler();
    }

    /**
     * 返回当前运行的 scx 实例的 scxMappingConfiguration
     *
     * @return a
     */
    public static ScxMappingConfiguration scxMappingConfiguration() {
        return scx().scxMappingConfiguration();
    }

    /**
     * 简化方法
     *
     * @param scxFeature a
     * @return a
     */
    public static boolean getFeatureState(ScxFeature scxFeature) {
        return featureConfig().getFeatureState(scxFeature);
    }

    /**
     * 简化方法
     *
     * @return a
     */
    public static SQLRunner sqlRunner() {
        return dao().sqlRunner();
    }

    /**
     * 简化方法
     *
     * @param requiredType r
     * @param <T>          r
     * @return r
     */
    public static <T> T getBean(Class<T> requiredType) {
        return beanFactory().getBean(requiredType);
    }

    /**
     * 简化方法
     *
     * @param clazz a {@link java.lang.Class} object
     * @param <T>   a T class
     * @return a {@link cool.scx.ScxModuleMetadata} object
     */
    public static <T extends ScxModule> T findScxModule(Class<T> clazz) {
        return scx().findScxModule(clazz);
    }

    /**
     * 简化方法
     *
     * @param path a
     * @return a
     */
    public static Path getPathByAppRoot(String path) {
        return environment().getPathByAppRoot(path);
    }

    /**
     * 获取临时路径
     *
     * @return a
     */
    public static Path getTempPath() {
        return environment().getTempPath();
    }

    /**
     * 获取临时路径
     *
     * @param paths a
     * @return a
     */
    public static Path getTempPath(String... paths) {
        return environment().getTempPath(paths);
    }

}
