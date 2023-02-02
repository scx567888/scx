package cool.scx.core;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.zaxxer.hikari.HikariDataSource;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxConfigSource;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.core.enumeration.ScxCoreFeature;
import cool.scx.core.eventbus.MessageCodecRegistrar;
import cool.scx.core.scheduler.ScxScheduler;
import cool.scx.dao.ScxDaoTableInfo;
import cool.scx.mvc.ScxMvc;
import cool.scx.mvc.ScxMvcOptions;
import cool.scx.mvc.websocket.ScxWebSocketRouter;
import cool.scx.sql.SQLHelper;
import cool.scx.sql.SQLRunner;
import cool.scx.util.ConsoleUtils;
import cool.scx.util.NetUtils;
import cool.scx.util.StopWatch;
import cool.scx.util.ansi.Ansi;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import javax.sql.DataSource;
import java.net.BindException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

import static cool.scx.core.ScxVersion.SCX_VERSION;

/**
 * 启动类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class Scx {

    private static final Logger logger = LoggerFactory.getLogger(Scx.class);

    private final ScxEnvironment scxEnvironment;

    private final String appKey;

    private final ScxFeatureConfig scxFeatureConfig;

    private final ScxConfig scxConfig;

    private final ScxModule[] scxModules;

    private final ScxOptions scxOptions;

    private final Vertx vertx;

    private final DefaultListableBeanFactory beanFactory;

    private final DataSource dataSource;

    private final SQLRunner sqlRunner;

    private final ScxMvc scxMvc;

    private final ScxScheduler scxScheduler;

    private ScxHttpRouter scxHttpRouter = null;

    private ScxWebSocketRouter scxWebSocketRouter = null;

    private HttpServer vertxHttpServer = null;

    Scx(ScxEnvironment scxEnvironment, String appKey, ScxFeatureConfig scxFeatureConfig, ScxConfigSource[] scxConfigSources, ScxModule[] scxModules) {
        //0, 赋值到全局
        ScxContext.scx(this);
        //1, 初始化基本参数
        this.scxEnvironment = scxEnvironment;
        this.appKey = appKey;
        this.scxFeatureConfig = scxFeatureConfig;
        this.scxConfig = new ScxConfig(scxConfigSources);
        this.scxModules = initScxModuleMetadataList(scxModules);
        this.scxOptions = new ScxOptions(this.scxConfig, this.scxEnvironment, this.appKey);
        //2, 初始化 ScxLog 日志框架
        ScxLoggerConfiguration.init(this.scxConfig, this.scxEnvironment);
        //3, 初始化 Vertx 这里在 log4j2 之后初始化是因为 vertx 需要使用 log4j2 打印日志
        this.vertx = initVertx();
        //4, 初始化事件总线
        MessageCodecRegistrar.registerCodec(this.vertx.eventBus());
        //5, 初始化 BeanFactory
        this.beanFactory = initBeanFactory(this.scxModules, this.vertx.nettyEventLoopGroup(), this.scxFeatureConfig);
        //6, 初始化持久层
        this.dataSource = getHikariDataSource(getMySQLDataSource(this.scxOptions));
        this.sqlRunner = new SQLRunner(this.dataSource);
        this.beanFactory.registerSingleton("sqlRunner", sqlRunner);
        //7, 初始化 MVC
        this.scxMvc = new ScxMvc(new ScxMvcOptions().templateRoot(scxOptions.templateRoot()).useDevelopmentErrorPage(scxFeatureConfig.get(ScxCoreFeature.USE_DEVELOPMENT_ERROR_PAGE)));
        //8, 初始化任务调度器
        this.scxScheduler = new ScxScheduler(this.vertx.nettyEventLoopGroup());
    }

    /**
     * 在控制台上打印 banner
     */
    static void printBanner() {
        Ansi.out()
                .red("   ▄████████ ").green(" ▄████████ ").blue("▀████    ▐████▀ ").ln()
                .red("  ███    ███ ").green("███    ███ ").blue("  ███▌   ████▀  ").ln()
                .red("  ███    █▀  ").green("███    █▀  ").blue("   ███  ▐███    ").ln()
                .red("  ███        ").green("███        ").blue("   ▀███▄███▀    ").ln()
                .red("▀███████████ ").green("███        ").blue("   ████▀██▄     ").ln()
                .red("         ███ ").green("███    █▄  ").blue("  ▐███  ▀███    ").ln()
                .red("   ▄█    ███ ").green("███    ███ ").blue(" ▄███     ███▄  ").ln()
                .red(" ▄████████▀  ").green("████████▀  ").blue("████       ███▄ ").cyan(" Version ").brightCyan(SCX_VERSION).println();
    }

    /**
     * 返回一个 builder 对象
     *
     * @return b
     */
    public static ScxBuilder builder() {
        return new ScxBuilder();
    }

    /**
     * 获取新的可用的端口号 (使用弹窗让用户进行选择)
     *
     * @param port a
     * @return a
     */
    private static boolean isUseNewPort(int port) {
        while (true) {
            var errMessage = """
                    *******************************************************
                    *                                                     *
                    *         端口号 [ %s ] 已被占用, 是否采用新端口号 ?       *
                    *                                                     *
                    *                [Y]es    |    [N]o                   *
                    *                                                     *
                    *******************************************************
                    """;
            System.err.printf((errMessage) + System.lineSeparator(), port);
            var result = ConsoleUtils.readLine().trim();
            if ("Y".equalsIgnoreCase(result)) {
                return true;
            } else if ("N".equalsIgnoreCase(result)) {
                var ignoreMessage = """
                        *******************************************
                        *                                         *
                        *     N 端口号被占用!!! 服务器启动失败 !!!      *
                        *                                         *
                        *******************************************
                        """;
                System.err.println(ignoreMessage);
                System.exit(-1);
                return false;
            }
        }
    }

    /**
     * 初始化 bean 工厂
     *
     * @param modules                  s
     * @param scheduledExecutorService s
     * @param scxFeatureConfig         a
     * @return r
     */
    private static DefaultListableBeanFactory initBeanFactory(ScxModule[] modules, ScheduledExecutorService scheduledExecutorService, ScxFeatureConfig scxFeatureConfig) {
        var beanFactory = new DefaultListableBeanFactory();
        //这里添加一个 bean 的后置处理器以便可以使用 @Autowired 注解
        var beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        beanPostProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(beanPostProcessor);
        //只有 开启标识时才 启用定时任务 这里直接跳过 后置处理器
        if (scxFeatureConfig.get(ScxCoreFeature.ENABLE_SCHEDULING_WITH_ANNOTATION)) {
            //这里在添加一个 bean 的后置处理器 以便使用 定时任务 注解
            var scheduledAnnotationBeanPostProcessor = new ScheduledAnnotationBeanPostProcessor();
            scheduledAnnotationBeanPostProcessor.setBeanFactory(beanFactory);
            scheduledAnnotationBeanPostProcessor.setScheduler(scheduledExecutorService);
            scheduledAnnotationBeanPostProcessor.afterSingletonsInstantiated();
            beanFactory.addBeanPostProcessor(scheduledAnnotationBeanPostProcessor);
        }
        //设置是否允许循环依赖 (默认禁止循环依赖)
        beanFactory.setAllowCircularReferences(scxFeatureConfig.get(ScxCoreFeature.ALLOW_CIRCULAR_REFERENCES));
        //注册 bean
        var beanClass = Arrays.stream(modules)
                .flatMap(c -> c.classList().stream())
                .filter(ScxHelper::isBeanClass)
                .toArray(Class<?>[]::new);

        for (var c : beanClass) {
            var beanDefinition = new AnnotatedGenericBeanDefinition(c);
            //这里是为了兼容 spring context 的部分注解
            AnnotationConfigUtils.processCommonDefinitionAnnotations(beanDefinition);
            beanFactory.registerBeanDefinition(c.getName(), beanDefinition);
        }
        return beanFactory;
    }

    /**
     * <p>initVertx.</p>
     *
     * @return a {@link io.vertx.core.Vertx} object
     */
    private static Vertx initVertx() {
        var vertxOptions = new VertxOptions();
        return Vertx.vertx(vertxOptions);
    }

    /**
     * <p>initScxModuleMetadataList.</p>
     *
     * @param scxModules an array of {@link cool.scx.core.ScxModule} objects
     * @return a {@link java.util.List} object
     */
    private static ScxModule[] initScxModuleMetadataList(ScxModule[] scxModules) {
        //2, 检查模块参数是否正确
        if (scxModules == null || Arrays.stream(scxModules).noneMatch(Objects::nonNull)) {
            throw new IllegalArgumentException("Modules must not be empty !!!");
        }
        return scxModules;
    }

    /**
     * HikariDataSource 初始化 HikariDataSource 数据源 （此处内部使用连接池提高性能）
     *
     * @param mysqlDataSource s
     * @return s
     */
    private static DataSource getHikariDataSource(MysqlDataSource mysqlDataSource) {
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setDataSource(mysqlDataSource);
        return hikariDataSource;
    }

    /**
     * 初始化 MySQL 数据源
     *
     * @param scxOptions a {@link cool.scx.core.ScxOptions} object
     * @return MySQL 数据源
     */
    private static MysqlDataSource getMySQLDataSource(ScxOptions scxOptions) {
        var mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setServerName(scxOptions.dataSourceHost());
        mysqlDataSource.setDatabaseName(scxOptions.dataSourceDatabase());
        mysqlDataSource.setUser(scxOptions.dataSourceUsername());
        mysqlDataSource.setPassword(scxOptions.dataSourcePassword());
        mysqlDataSource.setPort(scxOptions.dataSourcePort());
        // 设置参数值
        for (var parameter : scxOptions.dataSourceParameters()) {
            var p = parameter.split("=");
            if (p.length == 2) {
                var property = mysqlDataSource.getProperty(p[0]);
                property.setValue(property.getPropertyDefinition().parseObject(p[1], null));
            }
        }
        return mysqlDataSource;
    }

    /**
     * 数据源连接异常
     *
     * @param e a {@link java.lang.Exception} object.
     */
    public static void dataSourceExceptionHandler(Exception e) {
        while (true) {
            var errMessage = """
                    **************************************************************
                    *                                                            *
                    *           X 数据源连接失败 !!! 是否忽略错误并继续运行 ?            *
                    *                                                            *
                    *        [Y] 忽略错误并继续运行    |     [N] 退出程序              *
                    *                                                            *
                    **************************************************************
                    """;
            System.err.println(errMessage);
            var result = ConsoleUtils.readLine().trim();
            if ("Y".equalsIgnoreCase(result)) {
                var ignoreMessage = """
                        *******************************************
                        *                                         *
                        *       N 数据源链接错误,用户已忽略 !!!         *
                        *                                         *
                        *******************************************
                        """;
                System.err.println(ignoreMessage);
                break;
            } else if ("N".equalsIgnoreCase(result)) {
                e.printStackTrace();
                System.exit(-1);
                break;
            }
        }
    }

    /**
     * 执行模块启动的生命周期
     */
    private void startAllScxModules() {
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_MODULE_LIFE_CYCLE_INFO)) {
            for (var m : scxModules) {
                Ansi.out().brightWhite("[").brightGreen("Starting").brightWhite("] " + m.name()).println();
                m.start(this);
                Ansi.out().brightWhite("[").brightGreen("Start OK").brightWhite("] " + m.name()).println();
            }
        } else {
            for (var m : scxModules) {
                m.start(this);
            }
        }
    }

    /**
     * 执行模块结束的生命周期
     */
    private void stopAllScxModules() {
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_MODULE_LIFE_CYCLE_INFO)) {
            for (var m : scxModules) {
                Ansi.out().brightWhite("[").brightRed("Stopping").brightWhite("] " + m.name()).println();
                m.stop(this);
                Ansi.out().brightWhite("[").brightRed("Stop  OK").brightWhite("] " + m.name()).println();
            }
        } else {
            for (var m : scxModules) {
                m.stop(this);
            }
        }
    }

    /**
     * 运行项目
     */
    public void run() {
        //0, 启动 核心计时器
        StopWatch.start("ScxRun");
        //1, 根据配置打印一下 banner 或者配置文件信息之类
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_BANNER)) {
            printBanner();
        }
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_OPTIONS_INFO)) {
            this.scxOptions.printInfo();
        }
        //2, 初始化路由 (Http 和 WebSocket)
        this.scxHttpRouter = new ScxHttpRouter(this);
        this.scxWebSocketRouter = new ScxWebSocketRouter();
        //3, 注册 ScxMapping 和 ScxWebSocketMapping 注解的 handler 到 路由中去
        var classList = Arrays.stream(this.scxModules()).flatMap(c -> c.classList().stream()).toList();
        this.scxMvc.bindErrorHandler(this.scxHttpRouter).registerHttpRoutes(scxHttpRouter, beanFactory, classList).registerWebSocketRoutes(scxWebSocketRouter, beanFactory, classList);
        //4, 依次执行 模块的 start 生命周期 , 在这里我们可以操作 scxRouteRegistry, vertxRouter 等对象 "手动注册新路由" 或其他任何操作
        this.startAllScxModules();
        //5, 打印基本信息
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_START_UP_INFO)) {
            Ansi.out()
                    .brightYellow("已加载 " + this.beanFactory.getBeanDefinitionNames().length + " 个 Bean !!!").ln()
                    .brightGreen("已加载 " + this.scxHttpRouter.getRoutes().size() + " 个 Http 路由 !!!").ln()
                    .brightBlue("已加载 " + this.scxWebSocketRouter.getRoutes().size() + " 个 WebSocket 路由 !!!").println();
        }
        //6, 初始化服务器
        var httpServerOptions = new HttpServerOptions();
        if (this.scxOptions.isHttpsEnabled()) {
            httpServerOptions.setSsl(true)
                    .setKeyStoreOptions(new JksOptions()
                            .setPath(this.scxOptions.sslPath().toString())
                            .setPassword(this.scxOptions.sslPassword()));
        }
        this.vertxHttpServer = vertx.createHttpServer(httpServerOptions);
        this.vertxHttpServer.requestHandler(this.scxHttpRouter).webSocketHandler(this.scxWebSocketRouter);
        //7, 添加程序停止时的钩子函数
        this.addShutdownHook();
        //8, 使用初始端口号 启动服务器
        this.startServer(this.scxOptions.port());
        //9, 此处刷新 scxBeanFactory 使其实例化所有符合条件的 Bean
        this.beanFactory.preInstantiateSingletons();
    }

    /**
     * 启动服务器
     *
     * @param port a int
     */
    private void startServer(int port) {
        var listenFuture = this.vertxHttpServer.listen(port);
        listenFuture.onSuccess(server -> {
            var httpOrHttps = this.scxOptions.isHttpsEnabled() ? "https" : "http";
            var o = Ansi.out().green("服务器启动成功... 用时 " + StopWatch.stopToMillis("ScxRun") + " ms").ln();
            var normalIP = NetUtils.getLocalIPAddress().getNormalIP();
            for (var ip : normalIP) {
                o.green("> 网络: " + httpOrHttps + "://" + ip + ":" + server.actualPort() + "/").ln();
            }
            o.green("> 本地: " + httpOrHttps + "://localhost:" + server.actualPort() + "/").println();
        }).onFailure(cause -> {
            if (cause instanceof BindException) {
                //获取新的端口号然后 重新启动服务器
                if (isUseNewPort(port)) {
                    startServer(0);
                }
            } else {
                cause.printStackTrace();
            }
        });
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.stopAllScxModules();
            Ansi.out().red("项目正在停止!!!").println();
        }));
    }

    /**
     * 检查数据源是否可用
     *
     * @return b
     */
    public boolean checkDataSource() {
        try (var conn = dataSource.getConnection()) {
            var dm = conn.getMetaData();
            logger.debug("数据源连接成功 : 类型 [{}]  版本 [{}]", dm.getDatabaseProductName(), dm.getDatabaseProductVersion());
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
        logger.debug("修复数据表结构中...");
        var databaseName = this.scxOptions.dataSourceDatabase();
        //修复成功的表
        var fixSuccess = 0;
        //修复失败的表
        var fixFail = 0;
        //不需要修复的表
        var noNeedToFix = 0;
        for (var v : getAllScxBaseModelClassList()) {
            //根据 class 获取 tableInfo
            var tableInfo = new ScxDaoTableInfo(v);
            try {
                if (SQLHelper.checkNeedFixTable(tableInfo, databaseName, dataSource)) {
                    SQLHelper.fixTable(tableInfo, databaseName, dataSource);
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
            logger.debug("修复成功 {} 张表...", fixSuccess);
        }
        if (fixFail != 0) {
            logger.warn("修复失败 {} 张表...", fixFail);
        }
        if (fixSuccess + fixFail == 0) {
            logger.debug("没有表需要修复...");
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
        logger.debug("检查数据表结构中...");
        var databaseName = this.scxOptions.dataSourceDatabase();
        for (var v : getAllScxBaseModelClassList()) {
            //根据 class 获取 tableInfo
            var tableInfo = new ScxDaoTableInfo(v);
            try {
                //有任何需要修复的直接 返回 true
                if (SQLHelper.checkNeedFixTable(tableInfo, databaseName, dataSource)) {
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

    public Vertx vertx() {
        return vertx;
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
        return dataSource;
    }

    public SQLRunner sqlRunner() {
        return sqlRunner;
    }

    public HttpServer vertxHttpServer() {
        return vertxHttpServer;
    }

    public EventBus eventBus() {
        return vertx.eventBus();
    }

    public ScxWebSocketRouter scxWebSocketRouter() {
        return scxWebSocketRouter;
    }

    public ScxMvc scxMvc() {
        return scxMvc;
    }

    public ScxScheduler scxScheduler() {
        return scxScheduler;
    }

}
