package cool.scx;

import cool.scx.config.ScxConfig;
import cool.scx.config.ScxConfigSource;
import cool.scx.config.ScxEasyConfig;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.dao.ScxDao;
import cool.scx.enumeration.ScxFeature;
import cool.scx.eventbus.ScxEventBus;
import cool.scx.http.ScxHttpRouter;
import cool.scx.logging.ScxLoggerConfiguration;
import cool.scx.mvc.ScxMappingConfiguration;
import cool.scx.scheduler.ScxScheduler;
import cool.scx.util.ConsoleUtils;
import cool.scx.util.NetUtils;
import cool.scx.util.StopWatch;
import cool.scx.util.ansi.Ansi;
import cool.scx.websocket.ScxWebSocketRouter;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;

import java.io.IOException;
import java.net.BindException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 启动类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class Scx {

    /**
     * 项目根模块 所在路径
     * 默认取 所有自定义模块的最后一个 所在的文件根目录
     */
    private final ScxEnvironment scxEnvironment;

    /**
     * 项目的 appKey
     */
    private final String appKey;

    /**
     * Scx 特性配置文件
     */
    private final ScxFeatureConfig scxFeatureConfig;

    /**
     * scxConfig
     */
    private final ScxConfig scxConfig;

    /**
     * ScxModule 描述集合
     */
    private final List<ScxModuleInfo<?>> scxModuleInfos;

    /**
     * scxEasyConfig
     */
    private final ScxEasyConfig scxEasyConfig;

    /**
     * vertx
     */
    private final Vertx vertx;

    /**
     * s
     */
    private final ScxEventBus scxEventBus;

    /**
     * scxBeanFactory
     */
    private final ScxBeanFactory scxBeanFactory;

    /**
     * 模板对象
     */
    private final ScxTemplate scxTemplate;

    /**
     * dao
     */
    private final ScxDao scxDao;

    /**
     * a
     */
    private final ScxMappingConfiguration scxMappingConfiguration;

    /**
     * 任务调度器
     */
    private final ScxScheduler scxScheduler;

    /**
     * 路由
     */
    private ScxHttpRouter scxHttpRouter = null;

    /**
     * websocket 路由
     */
    private ScxWebSocketRouter scxWebSocketRouter = null;

    /**
     * 后台服务器
     */
    private HttpServer vertxHttpServer = null;

    /**
     * 初始化 Scx
     *
     * @param scxEnvironment   m
     * @param appKey           a
     * @param scxFeatureConfig f
     * @param scxConfigSources f
     * @param scxModules       s
     */
    Scx(ScxEnvironment scxEnvironment, String appKey, ScxFeatureConfig scxFeatureConfig, ScxConfigSource[] scxConfigSources, ScxModule[] scxModules) {
        //0, 赋值到全局
        ScxContext.scx(this);
        //1, 初始化基本参数
        this.scxEnvironment = scxEnvironment;
        this.appKey = appKey;
        this.scxFeatureConfig = scxFeatureConfig;
        this.scxConfig = new ScxConfig(scxConfigSources);
        this.scxModuleInfos = initScxModuleInfos(scxModules);
        this.scxEasyConfig = new ScxEasyConfig(this.scxConfig, this.scxEnvironment, this.appKey);
        //2, 初始化 ScxLog 日志框架
        ScxLoggerConfiguration.init(this.scxConfig, this.scxEnvironment);
        //3, 初始化 Vertx 这里在 log4j2 之后初始化是因为 vertx 需要使用 log4j2 打印日志
        this.vertx = initVertx();
        //4, 初始化事件总线 (这里的 ScxEventBus 其实只是针对 vertx 的 eventBus 进行一次包装)
        this.scxEventBus = new ScxEventBus(this.vertx);
        //5, 初始化 BeanFactory
        this.scxBeanFactory = new ScxBeanFactory(this.vertx.nettyEventLoopGroup(), this.scxFeatureConfig);
        //6, 注册 Bean 并刷新 BeanFactory
        initScxBeanFactory(this.scxBeanFactory, this.scxModuleInfos);
        //7, 初始化模板
        this.scxTemplate = new ScxTemplate(this.scxEasyConfig);
        //8, 初始化持久层
        this.scxDao = new ScxDao(this.scxEasyConfig, this.scxFeatureConfig);
        //9, ScxMapping 配置类
        this.scxMappingConfiguration = new ScxMappingConfiguration();
        //10, 初始化任务调度器
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
                .red(" ▄████████▀  ").green("████████▀  ").blue("████       ███▄ ").cyan(" Version ").brightCyan(ScxConstant.SCX_VERSION).println();
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
     * @param scxBeanFactory a
     * @param scxModuleInfos s
     */
    private void initScxBeanFactory(ScxBeanFactory scxBeanFactory, List<ScxModuleInfo<?>> scxModuleInfos) {
        for (var s : scxModuleInfos) {
            scxBeanFactory.registerBeanDefinition(s.needRegisterBeanClassList().toArray(Class[]::new));
        }
        //此处刷新 bean
        scxBeanFactory.refresh();
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
     * <p>initScxModuleInfos.</p>
     *
     * @param scxModules an array of {@link cool.scx.ScxModule} objects
     * @return a {@link java.util.List} object
     */
    private static List<ScxModuleInfo<? extends ScxModule>> initScxModuleInfos(ScxModule[] scxModules) {
        //2, 检查模块参数是否正确
        if (scxModules == null || Arrays.stream(scxModules).noneMatch(Objects::nonNull)) {
            throw new IllegalArgumentException("Modules must not be empty !!!");
        }
        var tempScxModuleInfoList = new ArrayList<ScxModuleInfo<? extends ScxModule>>();
        //循环加载 module
        for (var module : scxModules) {
            if (module != null) {
                try {
                    tempScxModuleInfoList.add(new ScxModuleInfo<>(module));
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempScxModuleInfoList;
    }

    /**
     * 执行模块启动的生命周期
     */
    private void startAllModules() {
        if (this.scxFeatureConfig.getFeatureState(ScxFeature.SHOW_MODULE_LIFE_CYCLE_INFO)) {
            for (var scxModuleInfo : scxModuleInfos) {
                Ansi.out().brightWhite("[").brightGreen("Starting").brightWhite("] " + scxModuleInfo.scxModuleName()).println();
                scxModuleInfo.scxModuleExample().start();
                Ansi.out().brightWhite("[").brightGreen("Start OK").brightWhite("] " + scxModuleInfo.scxModuleName()).println();
            }
        } else {
            for (var scxModuleInfo : scxModuleInfos) {
                scxModuleInfo.scxModuleExample().start();
            }
        }
    }

    /**
     * 执行模块结束的生命周期
     */
    private void stopAllModules() {
        if (this.scxFeatureConfig.getFeatureState(ScxFeature.SHOW_MODULE_LIFE_CYCLE_INFO)) {
            for (var scxModuleInfo : scxModuleInfos) {
                Ansi.out().brightWhite("[").brightRed("Stopping").brightWhite("] " + scxModuleInfo.scxModuleName()).println();
                scxModuleInfo.scxModuleExample().stop();
                Ansi.out().brightWhite("[").brightRed("Stop  OK").brightWhite("] " + scxModuleInfo.scxModuleName()).println();
            }
        } else {
            for (var scxModuleInfo : scxModuleInfos) {
                scxModuleInfo.scxModuleExample().stop();
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
        if (this.scxFeatureConfig.getFeatureState(ScxFeature.SHOW_BANNER)) {
            printBanner();
        }
        if (this.scxFeatureConfig.getFeatureState(ScxFeature.SHOW_EASY_CONFIG_INFO)) {
            this.scxEasyConfig.showEasyConfigInfo();
        }
        //2, 初始化路由 (Http 和 WebSocket)
        this.scxHttpRouter = new ScxHttpRouter(this.scxMappingConfiguration, this.scxEasyConfig, this.vertx, this.scxModuleInfos, this.scxBeanFactory);
        this.scxWebSocketRouter = new ScxWebSocketRouter(this.scxModuleInfos, this.scxBeanFactory);
        //3, 依次执行 模块的 start 生命周期 , 在这里我们可以操作 scxRouteRegistry, vertxRouter 等对象 "手动注册新路由" 或其他任何操作
        this.startAllModules();
        //4, 打印基本信息
        if (this.scxFeatureConfig.getFeatureState(ScxFeature.SHOW_START_UP_INFO)) {
            Ansi.out()
                    .color("已加载 " + this.scxBeanFactory.getBeanDefinitionNames().length + " 个 Bean !!!").ln()
                    .color("已加载 " + this.scxHttpRouter.vertxRouter().getRoutes().size() + " 个 Http 路由 !!!").ln()
                    .color("已加载 " + this.scxWebSocketRouter.getRoutes().size() + " 个 WebSocket 路由 !!!").println();
        }
        //5, 初始化服务器
        var httpServerOptions = new HttpServerOptions();
        if (this.scxEasyConfig.isHttpsEnabled()) {
            httpServerOptions.setSsl(true)
                    .setKeyStoreOptions(new JksOptions()
                            .setPath(this.scxEasyConfig.sslPath().getPath())
                            .setPassword(this.scxEasyConfig.sslPassword()));
        }
        this.vertxHttpServer = vertx.createHttpServer(httpServerOptions);
        this.vertxHttpServer.requestHandler(this.scxHttpRouter.vertxRouter()).webSocketHandler(this.scxWebSocketRouter);
        //6, 添加程序停止时的钩子函数
        this.addShutdownHook();
        //7, 使用初始端口号 启动服务器
        this.startServer(this.scxEasyConfig.port());
    }

    /**
     * 启动服务器
     *
     * @param port a int
     */
    private void startServer(int port) {
        this.vertxHttpServer.listen(port, http -> {
            if (http.succeeded()) {
                var httpOrHttps = this.scxEasyConfig.isHttpsEnabled() ? "https" : "http";
                Ansi.out().green("服务器启动成功... 用时 " + StopWatch.stopToMillis("ScxRun") + " ms").ln()
                        .green("> 网络 : " + httpOrHttps + "://" + NetUtils.getLocalAddress() + ":" + this.vertxHttpServer.actualPort() + "/").ln()
                        .green("> 本地 : " + httpOrHttps + "://localhost:" + this.vertxHttpServer.actualPort() + "/").println();
            } else {
                var cause = http.cause();
                if (cause instanceof BindException) {
                    //获取新的端口号然后 重新启动服务器
                    if (isUseNewPort(port)) {
                        startServer(0);
                    }
                } else {
                    cause.printStackTrace();
                }
            }
        });
    }

    /**
     * a
     *
     * @return a
     */
    public ScxEnvironment scxEnvironment() {
        return scxEnvironment;
    }

    /**
     * a
     *
     * @return a
     */
    public Vertx vertx() {
        return vertx;
    }

    /**
     * a
     *
     * @return a
     */
    public String appKey() {
        return appKey;
    }

    /**
     * a
     *
     * @return a
     */
    public List<ScxModuleInfo<? extends ScxModule>> scxModuleInfos() {
        return scxModuleInfos;
    }

    /**
     * <p>findScxModuleInfo.</p>
     *
     * @param clazz a {@link java.lang.Class} object
     * @param <T>   a T class
     * @return a {@link cool.scx.ScxModuleInfo} object
     */
    @SuppressWarnings("unchecked")
    public <T extends ScxModule> ScxModuleInfo<T> findScxModuleInfo(Class<T> clazz) {
        return (ScxModuleInfo<T>) scxModuleInfos.stream().filter(s -> s.scxModuleClass() == clazz).findAny().orElse(null);
    }

    /**
     * a
     *
     * @return a
     */
    public ScxEasyConfig scxEasyConfig() {
        return scxEasyConfig;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxBeanFactory scxBeanFactory() {
        return scxBeanFactory;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxHttpRouter scxHttpRouter() {
        return this.scxHttpRouter;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxTemplate scxTemplate() {
        return scxTemplate;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxConfig scxConfig() {
        return scxConfig;
    }

    /**
     * 获取特性的值 如果没有显式设置 则返回默认值
     *
     * @return s
     */
    public ScxFeatureConfig scxFeatureConfig() {
        return scxFeatureConfig;
    }

    /**
     * dao
     *
     * @return dao
     */
    public ScxDao scxDao() {
        return scxDao;
    }

    /**
     * server
     *
     * @return vertxHttpServer
     */
    public HttpServer vertxHttpServer() {
        return vertxHttpServer;
    }

    /**
     * server
     *
     * @return ScxServer
     */
    public ScxEventBus scxEventBus() {
        return scxEventBus;
    }

    /**
     * web
     *
     * @return w
     */
    public ScxWebSocketRouter scxWebSocketRouter() {
        return scxWebSocketRouter;
    }

    /**
     * a
     *
     * @return a
     */
    public ScxMappingConfiguration scxMappingConfiguration() {
        return scxMappingConfiguration;
    }

    /**
     * 添加监听事件
     * 目前只监听项目停止事件
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.stopAllModules();
            Ansi.out().red("项目正在停止!!!").println();
        }));
    }

    /**
     * a
     *
     * @return a
     */
    public ScxScheduler scxScheduler() {
        return scxScheduler;
    }

}
