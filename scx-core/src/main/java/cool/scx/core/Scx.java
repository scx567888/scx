package cool.scx.core;

import cool.scx.config.ScxConfig;
import cool.scx.config.ScxConfigSource;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.core.enumeration.ScxCoreFeature;
import cool.scx.core.eventbus.MessageCodecRegistrar;
import cool.scx.core.scheduler.ScxScheduler;
import cool.scx.mvc.ScxMvc;
import cool.scx.mvc.ScxMvcOptions;
import cool.scx.mvc.websocket.WebSocketRouter;
import cool.scx.orm.AnnotationConfigTable;
import cool.scx.orm.jdbc.SQLRunner;
import cool.scx.orm.jdbc.SchemaHelper;
import cool.scx.util.NetUtils;
import cool.scx.util.StopWatch;
import cool.scx.util.ansi.Ansi;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import javax.sql.DataSource;
import java.net.BindException;
import java.util.Arrays;
import java.util.List;

import static cool.scx.core.ScxHelper.*;

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

    private WebSocketRouter webSocketRouter = null;

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
        initScxLoggerFactory(this.scxConfig, this.scxEnvironment);
        //3, 初始化 Vertx 这里在 log4j2 之后初始化是因为 vertx 需要使用 log4j2 打印日志
        this.vertx = initVertx();
        //4, 初始化事件总线
        MessageCodecRegistrar.registerCodec(this.vertx.eventBus());
        //5, 初始化 BeanFactory
        this.beanFactory = initBeanFactory(this.scxModules, this.vertx.nettyEventLoopGroup(), this.scxFeatureConfig);
        //6, 初始化数据源及 sqlRunner
        this.dataSource = initDataSource(this.scxOptions, this.scxFeatureConfig);
        this.sqlRunner = new SQLRunner(this.dataSource);
        //7, 初始化 MVC
        this.scxMvc = new ScxMvc(new ScxMvcOptions().templateRoot(scxOptions.templateRoot()).useDevelopmentErrorPage(scxFeatureConfig.get(ScxCoreFeature.USE_DEVELOPMENT_ERROR_PAGE)));
        //8, 初始化任务调度器
        this.scxScheduler = new ScxScheduler(this.vertx.nettyEventLoopGroup());
    }

    public static ScxBuilder builder() {
        return new ScxBuilder();
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
        this.scxMvc.bindErrorHandler(this.scxHttpRouter).registerHttpRoutes(scxHttpRouter, beanFactory, classList).registerWebSocketRoutes(webSocketRouter, beanFactory, classList);
        //4, 依次执行 模块的 start 生命周期 , 在这里我们可以操作 scxRouteRegistry, vertxRouter 等对象 "手动注册新路由" 或其他任何操作
        this.startAllScxModules();
        //5, 打印基本信息
        if (this.scxFeatureConfig.get(ScxCoreFeature.SHOW_START_UP_INFO)) {
            Ansi.out()
                    .brightYellow("已加载 " + this.beanFactory.getBeanDefinitionNames().length + " 个 Bean !!!").ln()
                    .brightGreen("已加载 " + this.scxHttpRouter.getRoutes().size() + " 个 Http 路由 !!!").ln()
                    .brightBlue("已加载 " + this.webSocketRouter.getRoutes().size() + " 个 WebSocket 路由 !!!").println();
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
        this.vertxHttpServer.requestHandler(this.scxHttpRouter).webSocketHandler(this.webSocketRouter);
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
            var tableInfo = new AnnotationConfigTable(v);
            try {
                if (SchemaHelper.checkNeedFixTable(tableInfo, databaseName, dataSource)) {
                    SchemaHelper.fixTable(tableInfo, databaseName, dataSource);
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
            var tableInfo = new AnnotationConfigTable(v);
            try {
                //有任何需要修复的直接 返回 true
                if (SchemaHelper.checkNeedFixTable(tableInfo, databaseName, dataSource)) {
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

    public WebSocketRouter webSocketRouter() {
        return webSocketRouter;
    }

    public ScxMvc scxMvc() {
        return scxMvc;
    }

    public ScxScheduler scxScheduler() {
        return scxScheduler;
    }

    public <T> T getBean(Class<T> requiredType) {
        return beanFactory.getBean(requiredType);
    }

}
