package cool.scx.core;

import cool.scx.common.functional.ScxRunnable;
import cool.scx.common.scheduler.ScxScheduler;
import cool.scx.common.util.ScopedValue;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.core.enumeration.ScxCoreFeature;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.sql.SQLRunner;
import cool.scx.web.ScxWeb;
import cool.scx.http.routing.WebSocketRouter;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import org.springframework.beans.factory.BeanFactory;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * 用来存储 整个项目的上下文
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxContext {

    /**
     * 全局唯一的 SCX APP
     * <br>
     * 为了保证方法使用的简易 我们建议使用静态的方法
     * 但是其本质上是调用 GLOBAL_UNIQUE_SCX_APP 方法中的实例对象
     */
    final static ScopedValue<Scx> GLOBAL_SCX = ScopedValue.newInstance();

    /**
     * 兼容 旧版本 todo 待移除
     */
    private static Scx GLOBAL_SCX_0 = null;

    /**
     * 设置全局的 Scx 兼容 旧版本 todo 待移除
     *
     * @param scx scx
     */
    static void scx(Scx scx) {
        GLOBAL_SCX_0 = scx;
    }

    /**
     * 获取全局的 Scx
     *
     * @return scx
     */
    public static Scx scx() {
        if (GLOBAL_SCX.get() != null) {
            return GLOBAL_SCX.get();
        } else {
            // 兼容 旧版本 todo 待移除
            if (GLOBAL_SCX_0 != null) {
                return GLOBAL_SCX_0;
            }
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
    public static DataSource dataSource() {
        return scx().dataSource();
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
    public static EventBus eventBus() {
        return scx().eventBus();
    }

    /**
     * a
     *
     * @return a
     */
    public static WebSocketRouter webSocketRouter() {
        return scx().webSocketRouter();
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
    public static ScxModule[] scxModules() {
        return scx().scxModules();
    }

    /**
     * a
     *
     * @return a
     */
    public static ScxOptions options() {
        return scx().scxOptions();
    }

    /**
     * 返回当前运行的 scx 实例的 beanFactory
     *
     * @return a
     */
    public static BeanFactory beanFactory() {
        return scx().beanFactory();
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
    public static ScxWeb scxWeb() {
        return scx().scxWeb();
    }

    /**
     * 简化方法
     *
     * @param scxFeature a
     * @return a
     */
    public static boolean getFeatureState(ScxCoreFeature scxFeature) {
        return featureConfig().get(scxFeature);
    }

    /**
     * 简化方法
     *
     * @return a
     */
    public static SQLRunner sqlRunner() {
        return scx().sqlRunner();
    }

    /**
     * 避免冗长的 调用
     *
     * @param handler handler
     */
    public static void autoTransaction(ScxRunnable<?> handler) {
        sqlRunner().autoTransaction(handler);
    }

    /**
     * 避免冗长的 调用
     *
     * @param handler a
     * @param <T>     a
     * @return a
     */
    public static <T> T autoTransaction(Callable<T> handler) {
        return sqlRunner().autoTransaction(handler);
    }

    /**
     * 简化方法
     *
     * @param requiredType r
     * @param <T>          r
     * @return r
     */
    public static <T> T getBean(Class<T> requiredType) {
        return scx().getBean(requiredType);
    }

    /**
     * 简化方法
     *
     * @param clazz a {@link java.lang.Class} object
     * @param <T>   a T class
     * @return a 模块
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

    public static JDBCContext jdbcContext() {
        return scx().jdbcContext();
    }

}
