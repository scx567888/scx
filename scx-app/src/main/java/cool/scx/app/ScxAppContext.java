package cool.scx.app;

import cool.scx.app.enumeration.ScxAppFeature;
import cool.scx.app.eventbus.EventBus;
import cool.scx.bean.BeanFactory;
import cool.scx.common.util.ScopedValue;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.data.exception.DataAccessException;
import cool.scx.data.jdbc.JDBCTransactionContext;
import cool.scx.data.jdbc.JDBCTransactionManager;
import cool.scx.functional.ScxCallable;
import cool.scx.functional.ScxConsumer;
import cool.scx.functional.ScxFunction;
import cool.scx.functional.ScxRunnable;
import cool.scx.http.ScxHttpServer;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.sql.SQLRunner;
import cool.scx.web.ScxWeb;

import javax.sql.DataSource;
import java.nio.file.Path;

/// 用来存储 整个项目的上下文
///
/// @author scx567888
/// @version 0.0.1
public final class ScxAppContext {

    /// 全局唯一的 SCX APP
    /// 为了保证方法使用的简易 我们建议使用静态的方法
    /// 但是其本质上是调用 GLOBAL_UNIQUE_SCX_APP 方法中的实例对象
    final static ScopedValue<ScxApp> GLOBAL_SCX = ScopedValue.newInstance();

    /// 兼容 旧版本 todo 待移除
    private static ScxApp GLOBAL_SCX_0 = null;

    /// 设置全局的 Scx 兼容 旧版本 todo 待移除
    ///
    /// @param scx scx
    static void scx(ScxApp scx) {
        GLOBAL_SCX_0 = scx;
    }

    /// 获取全局的 Scx
    ///
    /// @return scx
    public static ScxApp scx() {
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

    public static ScxFeatureConfig featureConfig() {
        return scx().scxFeatureConfig();
    }

    public static ScxConfig config() {
        return scx().scxConfig();
    }

    public static DataSource dataSource() {
        return scx().dataSource();
    }

    public static ScxHttpServer httpServer() {
        return scx().httpServer();
    }

    public static EventBus eventBus() {
        return scx().eventBus();
    }

    public static ScxEnvironment environment() {
        return scx().scxEnvironment();
    }

    public static String appKey() {
        return scx().appKey();
    }

    public static ScxAppModule[] scxModules() {
        return scx().scxModules();
    }

    public static ScxAppOptions options() {
        return scx().scxOptions();
    }

    public static BeanFactory beanFactory() {
        return scx().beanFactory();
    }

    public static ScxAppHttpRouter router() {
        return scx().scxHttpRouter();
    }

    public static ScxWeb scxWeb() {
        return scx().scxWeb();
    }

    public static boolean getFeatureState(ScxAppFeature scxFeature) {
        return featureConfig().get(scxFeature);
    }

    public static SQLRunner sqlRunner() {
        return scx().sqlRunner();
    }

    public static <E extends Throwable> void autoTransaction(ScxRunnable<E> handler) throws E, DataAccessException {
        jdbcTransactionManager().autoTransaction(handler);
    }

    public static <T, E extends Throwable> T autoTransaction(ScxCallable<T, E> handler) throws E, DataAccessException {
        return jdbcTransactionManager().autoTransaction(handler);
    }

    public static <T, E extends Throwable> T withTransaction(ScxFunction<JDBCTransactionContext, T, E> handler) throws DataAccessException, E {
        return jdbcTransactionManager().withTransaction(handler);
    }

    public static <E extends Throwable> void withTransaction(ScxConsumer<JDBCTransactionContext, E> handler) throws DataAccessException, E {
        jdbcTransactionManager().withTransaction(handler);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return scx().getBean(requiredType);
    }

    public static <T extends ScxAppModule> T findScxModule(Class<T> clazz) {
        return scx().findScxModule(clazz);
    }

    public static Path getPathByAppRoot(String path) {
        return environment().getPathByAppRoot(path);
    }

    public static Path getTempPath() {
        return environment().getTempPath();
    }

    public static Path getTempPath(String... paths) {
        return environment().getTempPath(paths);
    }

    public static JDBCContext jdbcContext() {
        return scx().jdbcContext();
    }

    public static JDBCTransactionManager jdbcTransactionManager() {
        return scx().jdbcTransactionManager();
    }

}
