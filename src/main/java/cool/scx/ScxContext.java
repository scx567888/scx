package cool.scx;

import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEasyConfig;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.dao.ScxDao;
import cool.scx.enumeration.ScxFeature;
import cool.scx.eventbus.ScxEventBus;
import cool.scx.mvc.ScxMappingConfiguration;
import cool.scx.sql.SQLRunner;
import cool.scx.web.ScxWebSocketRouter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
     * appRoot
     *
     * @param path a
     * @return a
     */
    public static File getFileByAppRoot(String path) {
        return appRoot().getFileByAppRoot(path);
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
     * <p>findScxModuleInfo.</p>
     *
     * @param clazz a {@link java.lang.Class} object
     * @param <T>   a T class
     * @return a {@link ScxModuleInfo} object
     */
    public static <T extends ScxModule> ScxModuleInfo<T> findScxModuleInfo(Class<T> clazz) {
        return scx().findScxModuleInfo(clazz);
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
     * a
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
     * a
     *
     * @return a
     */
    public static ScxTemplate template() {
        return scx().scxTemplate();
    }

    /**
     * a
     *
     * @param scxFeature a
     * @return a
     */
    public static boolean getFeatureState(ScxFeature scxFeature) {
        return featureConfig().getFeatureState(scxFeature);
    }


    /**
     * 获取全局的 vertx
     *
     * @return 全局的事件总线
     */
    public static Vertx vertx() {
        return scx().vertx();
    }

    /**
     * 设置计时器
     * <p>
     * 本质上时内部调用 netty 的线程池完成
     * <p>
     * 因为java无法做到特别精确的计时所以此处单位采取 毫秒
     *
     * @param command 执行的事件
     * @param delay   延时执行的时间  单位毫秒
     * @return a
     */
    public static ScheduledFuture<?> schedule(Runnable command, long delay) {
        return vertx().nettyEventLoopGroup().schedule(command, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 循环执行一个任务
     * 只有当前任务执行完成后才会执行下一次任务 不用并行执行
     *
     * @param command      执行的任务
     * @param initialDelay 初始化延迟的时间 0 为立即执行
     * @param period       间隔的时间
     * @return a
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period) {
        return vertx().nettyEventLoopGroup().scheduleAtFixedRate(command, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 循环执行一个任务
     * 不管当前任务是否执行完成都会直接执行下一次任务 采用并行执行
     *
     * @param command      执行的任务
     * @param initialDelay 初始化延迟的时间 0 为立即执行
     * @param delay        间隔的时间
     */
    public static void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay) {
        vertx().nettyEventLoopGroup().scheduleWithFixedDelay(command, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行一个事件
     *
     * @param command 事件
     */
    public static void execute(Runnable command) {
        vertx().nettyEventLoopGroup().execute(command);
    }

    /**
     * a
     *
     * @param command a
     * @return a
     */
    public static Future<?> submit(Runnable command) {
        return vertx().nettyEventLoopGroup().submit(command);
    }

    /**
     * a
     *
     * @return a
     */
    public static SQLRunner sqlRunner() {
        return scx().scxDao().sqlRunner();
    }

    /**
     * a
     *
     * @return a
     */
    public static ScxMappingConfiguration scxMappingConfiguration() {
        return scx().scxMappingConfiguration();
    }

}
