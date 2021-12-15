package cool.scx;

import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEasyConfig;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.dao.ScxDao;
import cool.scx.enumeration.ScxFeature;
import cool.scx.eventbus.ScxEventBus;
import cool.scx.mvc.ScxMappingConfiguration;
import cool.scx.scheduler.ScxScheduler;
import cool.scx.sql.SQLRunner;
import cool.scx.web.ScxWebSocketRouter;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.io.File;
import java.util.List;

/**
 * 用来存储 整个项目的上下文
 */
public final class ScxContext {

    /**
     * 路由上下文 THREAD_LOCAL
     */
    private static final ThreadLocal<RoutingContext> ROUTING_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

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
    public static void routingContext(RoutingContext routingContext) {
        ROUTING_CONTEXT_THREAD_LOCAL.set(routingContext);
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
    public static ScxAppRoot appRoot() {
        return scx().scxAppRoot();
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
    public static List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos() {
        return scx().scxModuleInfos();
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
    public static Router router() {
        return scx().vertxRouter();
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
     * @return a {@link ScxModuleInfo} object
     */
    public static <T extends ScxModule> ScxModuleInfo<T> findScxModuleInfo(Class<T> clazz) {
        return scx().findScxModuleInfo(clazz);
    }

    /**
     * 简化方法
     *
     * @param path a
     * @return a
     */
    public static File getFileByAppRoot(String path) {
        return appRoot().getFileByAppRoot(path);
    }

}
