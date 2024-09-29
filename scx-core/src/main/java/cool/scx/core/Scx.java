package cool.scx.core;

import cool.scx.ansi.Ansi;
import cool.scx.common.util.FileUtils;
import cool.scx.common.util.ScopedValue;
import cool.scx.common.util.StopWatch;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.core.enumeration.ScxCoreFeature;
import cool.scx.core.eventbus.EventBus;
import cool.scx.data.jdbc.AnnotationConfigTable;
import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.helidon.HelidonHttpServer;
import cool.scx.http.routing.WebSocketRouter;
import cool.scx.jdbc.JDBCContext;
import cool.scx.jdbc.meta_data.SchemaHelper;
import cool.scx.jdbc.sql.SQLRunner;
import cool.scx.web.RouteRegistrar;
import cool.scx.web.ScxWeb;
import cool.scx.web.ScxWebOptions;
import cool.scx.web.WebSocketRouteRegistrar;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import javax.sql.DataSource;
import java.lang.System.Logger;
import java.net.BindException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import static cool.scx.common.util.NetUtils.getLocalIPAddress;
import static cool.scx.common.util.ScxExceptionHelper.ignore;
import static cool.scx.core.ScxContext.GLOBAL_SCX;
import static cool.scx.core.ScxHelper.*;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;

/**
 * 启动类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class Scx {

    static final Logger logger = System.getLogger(Scx.class.getName());

    /**
     * 默认 http 请求 body 限制大小
     */
    private static final long DEFAULT_BODY_LIMIT = FileUtils.displaySizeToLong("16384KB");
    private final ScxEnvironment scxEnvironment;

    private final String appKey;

    private final ScxFeatureConfig scxFeatureConfig;

    private final ScxConfig scxConfig;

    private final ScxModule[] scxModules;

    private final ScxOptions scxOptions;

    private final DefaultListableBeanFactory beanFactory;

    private final ScxWeb scxWeb;

    private final ScxHttpServerOptions defaultHttpServerOptions;

    private final EventBus eventBus;

    private JDBCContext jdbcContext = null;

    private ScxHttpRouter scxHttpRouter = null;

    private WebSocketRouter webSocketRouter = null;

    private ScxHttpServer vertxHttpServer = null;

    Scx(ScxEnvironment scxEnvironment, String appKey, ScxFeatureConfig scxFeatureConfig, ScxConfig scxConfig, ScxModule[] scxModules, ScxHttpServerOptions defaultHttpServerOptions) {
        //0, 赋值到全局
        ScxContext.scx(this);
        //1, 初始化基本参数
        this.scxEnvironment = scxEnvironment;
        this.appKey = appKey;
        this.scxFeatureConfig = scxFeatureConfig;
        this.scxConfig = scxConfig;
        this.scxModules = initScxModuleMetadataList(scxModules);
        this.scxOptions = new ScxOptions(this.scxConfig, this.scxEnvironment, this.appKey);
        this.defaultHttpServerOptions = defaultHttpServerOptions;
        //2, 初始化 ScxLog 日志框架
        initScxLoggerFactory(this.scxConfig, this.scxEnvironment);
        //3, 初始化 BeanFactory
        this.beanFactory = initBeanFactory(this.scxModules, this.scxFeatureConfig);
        //4, 初始化事件总线
        this.eventBus = new EventBus();
        //4, 初始化 Web
        this.scxWeb = new ScxWeb(new ScxWebOptions().templateRoot(scxOptions.templateRoot()).useDevelopmentErrorPage(scxFeatureConfig.get(ScxCoreFeature.USE_DEVELOPMENT_ERROR_PAGE)));
    }

    public static ScxBuilder builder() {
        return new ScxBuilder();
    }

    /**
     * 执行模块启动的生命周期
     */
    private void startAllScxModules() {
        for (var m : scxModules) {
            if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_MODULE_LIFE_CYCLE_INFO)) {
                Ansi.ansi().brightWhite("[").brightGreen("Starting").brightWhite("] " + m.name()).println();
            }
            m.start(this);
            if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_MODULE_LIFE_CYCLE_INFO)) {
                Ansi.ansi().brightWhite("[").brightGreen("Start OK").brightWhite("] " + m.name()).println();
            }
        }
    }

    /**
     * 执行模块结束的生命周期
     */
    private void stopAllScxModules() {
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_MODULE_LIFE_CYCLE_INFO)) {
            for (var m : scxModules) {
                Ansi.ansi().brightWhite("[").brightRed("Stopping").brightWhite("] " + m.name()).println();
                m.stop(this);
                Ansi.ansi().brightWhite("[").brightRed("Stop  OK").brightWhite("] " + m.name()).println();
            }
        } else {
            for (var m : scxModules) {
                m.stop(this);
            }
        }
    }

    public Scx run() {
        return ScopedValue.where(GLOBAL_SCX, this).get(this::run0);
    }

    /**
     * 运行项目
     */
    private Scx run0() {
        //0, 启动 核心计时器
        StopWatch.start("ScxRun");
        //1, 根据配置打印一下 banner 或者配置文件信息之类
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_BANNER)) {
            ScxVersion.printBanner();
        }
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_OPTIONS_INFO)) {
            this.scxOptions.printInfo();
        }
        //2, 初始化路由器 (Http 和 WebSocket)
        this.scxHttpRouter = new ScxHttpRouter(this);
        this.webSocketRouter = new WebSocketRouter();
        //3, 注册 路由
        var classList = Arrays.stream(this.scxModules()).flatMap(c -> c.classList().stream()).toList();
        var httpRoutes = RouteRegistrar.filterClass(classList).stream().map(beanFactory::getBean).toArray();
        var webSocketRoutes = WebSocketRouteRegistrar.filterClass(classList).stream().map(beanFactory::getBean).toArray();
        this.scxWeb.bindErrorHandler(this.scxHttpRouter).registerHttpRoutes(scxHttpRouter, httpRoutes).registerWebSocketRoutes(webSocketRouter, webSocketRoutes);
        //4, 依次执行 模块的 start 生命周期 , 在这里我们可以操作 scxRouteRegistry, vertxRouter 等对象 "手动注册新路由" 或其他任何操作
        this.startAllScxModules();
        //5, 打印基本信息
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_START_UP_INFO)) {
            Ansi.ansi()
                    .brightYellow("已加载 " + this.beanFactory.getBeanDefinitionNames().length + " 个 Bean !!!").ln()
                    .brightGreen("已加载 " + this.scxHttpRouter.getRoutes().size() + " 个 Http 路由 !!!").ln()
                    .brightBlue("已加载 " + this.webSocketRouter.getRoutes().size() + " 个 WebSocket 路由 !!!").println();
        }
        //6, 初始化服务器
        var httpServerOptions = new ScxHttpServerOptions()
                .setMaxPayloadSize(DEFAULT_BODY_LIMIT)
                .setPort(this.scxOptions.port());
        if (this.scxOptions.isHttpsEnabled()) {
            var tls = getTls(this.scxOptions.sslPath(), this.scxOptions.sslPassword());
            httpServerOptions.setTLS(tls);
        }
        this.vertxHttpServer = new HelidonHttpServer(httpServerOptions);
        this.vertxHttpServer.requestHandler(this.scxHttpRouter).webSocketHandler(this.webSocketRouter);
        //7, 添加程序停止时的钩子函数
        this.addShutdownHook();
        //8, 使用初始端口号 启动服务器
        this.startServer(this.scxOptions.port());
        //9, 此处刷新 scxBeanFactory 使其实例化所有符合条件的 Bean
        this.beanFactory.preInstantiateSingletons();
        //10, 启动调度器注解
        if (scxFeatureConfig.get(ScxCoreFeature.ENABLE_SCHEDULING_WITH_ANNOTATION)) {
            startAnnotationScheduled(this.beanFactory);
        }
        return this;
    }

    /**
     * 启动服务器
     *
     * @param port a int
     */
    private void startServer(int port) {
        try {
            this.vertxHttpServer.start();
            var httpOrHttps = this.scxOptions.isHttpsEnabled() ? "https" : "http";
            var o = Ansi.ansi().green("服务器启动成功... 用时 " + StopWatch.stopToMillis("ScxRun") + " ms").ln();
            o.green("> 本地: " + httpOrHttps + "://localhost:" + this.vertxHttpServer.port() + "/").ln();
            var normalIP = ignore(() -> getLocalIPAddress(c -> c instanceof Inet4Address), new InetAddress[]{});
            for (var ip : normalIP) {
                o.green("> 网络: " + httpOrHttps + "://" + ip.getHostAddress() + ":" + this.vertxHttpServer.port() + "/").ln();
            }
            o.print();
        } catch (Exception cause) {
            if (cause instanceof BindException) {
                //获取新的端口号然后 重新启动服务器
                if (isUseNewPort(port)) {
                    startServer(0);
                }
            } else {
                cause.printStackTrace();
            }
        }
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.stopAllScxModules();
            Ansi.ansi().red("项目正在停止!!!").println();
        }));
    }

    /**
     * 检查数据源是否可用
     *
     * @return b
     */
    public boolean checkDataSource() {
        try (var conn = dataSource().getConnection()) {
            var dm = conn.getMetaData();
            logger.log(DEBUG, "数据源连接成功 : 类型 [{0}]  版本 [{1}]", dm.getDatabaseProductName(), dm.getDatabaseProductVersion());
            return true;
        } catch (Exception e) {
            dataSourceExceptionHandler(e);
            return false;
        }
    }

    /**
     * <p>fixTable.</p>
     */
    public void fixTable() {
        logger.log(DEBUG, "修复数据表结构中...");
        //修复成功的表
        var fixSuccess = 0;
        //修复失败的表
        var fixFail = 0;
        //不需要修复的表
        var noNeedToFix = 0;
        for (var v : getAllScxBaseModelClassList()) {
            //根据 class 获取 tableInfo
            var tableInfo = new AnnotationConfigTable(v);
            try {
                if (SchemaHelper.checkNeedFixTable(tableInfo, dataSource())) {
                    SchemaHelper.fixTable(tableInfo, jdbcContext);
                    fixSuccess = fixSuccess + 1;
                } else {
                    noNeedToFix = noNeedToFix + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                fixFail = fixFail + 1;
            }
        }

        if (fixSuccess != 0) {
            logger.log(DEBUG, "修复成功 {0} 张表...", fixSuccess);
        }
        if (fixFail != 0) {
            logger.log(WARNING, "修复失败 {0} 张表...", fixFail);
        }
        if (fixSuccess + fixFail == 0) {
            logger.log(DEBUG, "没有表需要修复...");
        }

    }

    /**
     * 获取所有 class
     *
     * @return s
     */
    private List<Class<?>> getAllScxBaseModelClassList() {
        return Arrays.stream(scxModules)
                .flatMap(c -> c.classList().stream())
                .filter(ScxHelper::isScxBaseModelClass)// 继承自 BaseModel
                .toList();
    }

    /**
     * 检查是否有任何 (BaseModel) 类需要修复表
     *
     * @return 是否有
     */
    public boolean checkNeedFixTable() {
        logger.log(DEBUG, "检查数据表结构中...");
        for (var v : getAllScxBaseModelClassList()) {
            //根据 class 获取 tableInfo
            var tableInfo = new AnnotationConfigTable(v);
            try {
                //有任何需要修复的直接 返回 true
                if (SchemaHelper.checkNeedFixTable(tableInfo, dataSource())) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T extends ScxModule> T findScxModule(Class<T> clazz) {
        for (var m : this.scxModules) {
            if (m.getClass() == clazz) {
                return (T) m;
            }
        }
        return null;
    }

    public ScxModule[] scxModules() {
        return Arrays.copyOf(scxModules, scxModules.length);
    }

    public ScxEnvironment scxEnvironment() {
        return scxEnvironment;
    }

    public String appKey() {
        return appKey;
    }

    public ScxOptions scxOptions() {
        return scxOptions;
    }

    public DefaultListableBeanFactory beanFactory() {
        return beanFactory;
    }

    public ScxHttpRouter scxHttpRouter() {
        return this.scxHttpRouter;
    }

    public ScxConfig scxConfig() {
        return scxConfig;
    }

    public ScxFeatureConfig scxFeatureConfig() {
        return scxFeatureConfig;
    }

    public DataSource dataSource() {
        return jdbcContext().dataSource();
    }

    public SQLRunner sqlRunner() {
        return jdbcContext().sqlRunner();
    }

    public JDBCContext jdbcContext() {
        if (jdbcContext == null) {
            //1, 初始化数据源及 sqlRunner
            var dataSource = initDataSource(this.scxOptions, this.scxFeatureConfig);
            this.jdbcContext = new JDBCContext(dataSource);
        }
        return jdbcContext;
    }

    public ScxHttpServer vertxHttpServer() {
        return vertxHttpServer;
    }

    public EventBus eventBus() {
        return eventBus;
    }

    public WebSocketRouter webSocketRouter() {
        return webSocketRouter;
    }

    public ScxWeb scxWeb() {
        return scxWeb;
    }

    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

}
