package cool.scx.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cool.scx.app.annotation.Scheduled;
import cool.scx.app.annotation.ScheduledList;
import cool.scx.app.annotation.ScxService;
import cool.scx.app.base.BaseModel;
import cool.scx.app.base.BaseModelService;
import cool.scx.bean.BeanFactory;
import cool.scx.bean.BeanFactoryImpl;
import cool.scx.bean.resolver.AutowiredAnnotationResolver;
import cool.scx.common.util.ClassUtils;
import cool.scx.common.util.ObjectUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.config.handler.AppRootHandler;
import cool.scx.config.handler.ConvertValueHandler;
import cool.scx.config.handler.DefaultValueHandler;
import cool.scx.data.jdbc.annotation.Table;
import cool.scx.jdbc.dialect.DialectSelector;
import cool.scx.jdbc.spy.Spy;
import cool.scx.logging.ScxLoggerConfig;
import cool.scx.logging.ScxLogging;
import cool.scx.logging.recorder.ConsoleRecorder;
import cool.scx.logging.recorder.FileRecorder;
import cool.scx.object.ScxObject;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.ScxReflect;
import cool.scx.reflect.TypeReference;
import cool.scx.scheduling.ScxScheduling;
import cool.scx.web.annotation.ScxRoute;
import cool.scx.web.annotation.ScxWebSocketRoute;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static cool.scx.app.enumeration.ScxAppFeature.USE_SPY;
import static cool.scx.common.util.ClassUtils.*;
import static cool.scx.reflect.AccessModifier.PUBLIC;
import static java.lang.System.Logger.Level.*;
import static java.util.Objects.requireNonNull;

/// ScxHelper
///
/// @author scx567888
/// @version 0.0.1
public final class ScxAppHelper {

    /// Constant <code>beanFilterAnnotation</code>
    private static final List<Class<? extends Annotation>> beanFilterAnnotation = List.of(
            //scx 注解
            ScxRoute.class, ScxService.class, ScxWebSocketRoute.class);

    static Path findRootPathByScxModule(Class<? extends ScxAppModule> c) throws IOException {
        var classSource = getCodeSource(c);
        var classSourcePath = Path.of(classSource);
        var isJar = isJar(classSourcePath);
        //判断当前是否处于 jar 包中 并使用不同的 方式加载
        return isJar ? classSourcePath.getParent() : classSourcePath;
    }

    /// 根据 ScxModule 的 class 查找 所有 class
    ///
    /// @param c c
    /// @return class 列表 (注意这里返回的是不可变的列表 !!!)
    /// @throws IOException r
    static List<Class<?>> findClassListByScxModule(Class<? extends ScxAppModule> c) throws IOException {
        var classSource = getCodeSource(c);
        var classSourcePath = Path.of(classSource);
        var isJar = isJar(classSourcePath);
        //判断当前是否处于 jar 包中 并使用不同的 方式加载
        var allClassList = isJar ? findClassListFromJar(classSource) : findClassListFromPath(classSourcePath, c.getClassLoader());
        //使用 basePackage 过滤
        var basePackage = c.getPackageName();
        return List.of(filterByBasePackage(allClassList, basePackage));
    }

    /// 拥有 scx 注解
    ///
    /// @param clazz class
    /// @return b
    public static boolean isBeanClass(Class<?> clazz) {
        for (var a : beanFilterAnnotation) {
            if (clazz.getAnnotation(a) != null) {
                return true;
            }
        }
        return false;
    }

    /// 初始化 ScxModelClassList
    ///
    /// @param c a
    /// @return a
    public static boolean isScxBaseModelClass(Class<?> c) {
        return c.isAnnotationPresent(Table.class) &&  // 拥有注解
                ClassUtils.isInstantiableClass(c) &&  // 是一个可以不需要其他参数直接生成实例化的对象
                BaseModel.class.isAssignableFrom(c);
    }

    public static boolean isScxBaseModelServiceClass(Class<?> c) {
        return c.isAnnotationPresent(ScxService.class) &&  // 拥有注解
                ClassUtils.isNormalClass(c) && // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
                c.getGenericSuperclass() instanceof ParameterizedType t && //需要有泛型参数
                t.getActualTypeArguments().length == 1; //并且泛型参数的数量必须是一个
    }

    @SuppressWarnings("unchecked")
    public static <Entity extends BaseModel> Class<Entity> findBaseModelServiceEntityClass(Class<?> baseModelServiceClass) {
        // todo 这里强转可能有问题
        var superClass = ((ClassInfo)ScxReflect.typeOf(baseModelServiceClass)).findSuperType(BaseModelService.class);
        if (superClass != null) {
            var boundType = superClass.bindings().get(0);
            if (boundType != null) {
                return (Class<Entity>) boundType.rawClass();
            } else {
                throw new IllegalArgumentException(baseModelServiceClass.getName() + " : 必须设置泛型参数 !!!");
            }
        } else {
            throw new IllegalArgumentException(baseModelServiceClass.getName() + " : 必须继承自 BaseModelService !!!");
        }
    }

    /// 获取新的可用的端口号 (使用弹窗让用户进行选择)
    ///
    /// @param port a
    /// @return a
    static boolean isUseNewPort(int port) {
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
            var result = System.console().readLine().trim();
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

    static DataSource initDataSource(ScxAppOptions scxOptions, ScxFeatureConfig scxFeatureConfig) {
        var url = scxOptions.dataSourceUrl();
        var dialect = DialectSelector.findDialect(url);
        var realDataSource = dialect.createDataSource(url, scxOptions.dataSourceUsername(), scxOptions.dataSourcePassword(), scxOptions.dataSourceParameters());
        //使用 HikariDataSource 进行包装
        var hikariConfig = new HikariConfig();
        hikariConfig.setDataSource(realDataSource);
        var hikariDataSource = new HikariDataSource(hikariConfig);
        return scxFeatureConfig.get(USE_SPY) ? Spy.wrap(hikariDataSource) : hikariDataSource;
    }

    static ScxAppModule[] initScxModuleMetadataList(ScxAppModule[] scxModules) {
        //2, 检查模块参数是否正确
        if (scxModules == null || Arrays.stream(scxModules).noneMatch(Objects::nonNull)) {
            throw new IllegalArgumentException("Modules must not be empty !!!");
        }
        return scxModules;
    }

    static BeanFactory initBeanFactory(ScxAppModule[] modules, ScxFeatureConfig scxFeatureConfig) {
        var beanFactory = new BeanFactoryImpl();
        //这里添加一个 bean 的后置处理器以便可以使用 @Autowired 注解
        beanFactory.addBeanResolver(new AutowiredAnnotationResolver(beanFactory));
        //注册 bean
        var beanClass = Arrays.stream(modules)
                .flatMap(c -> c.classList().stream())
                .filter(ScxAppHelper::isBeanClass)
                .toArray(Class<?>[]::new);

        for (var c : beanClass) {
            beanFactory.registerBeanClass(c.getName(), c);
        }
        return beanFactory;
    }

    public static void startAnnotationScheduled(BeanFactory beanFactory) {
        var beanDefinitionNames = beanFactory.getBeanNames();
        for (var beanDefinitionName : beanDefinitionNames) {
            var bean = beanFactory.getBean(beanDefinitionName);
            // todo 这里强转可能有问题
            var classInfo =(ClassInfo) ScxReflect.typeOf(bean.getClass());
            for (var method : classInfo.methods()) {
                if (method.accessModifier() != PUBLIC) {
                    continue;
                }
                var scheduledList = Arrays.stream(method.annotations()).flatMap(c -> switch (c) {
                    case Scheduled s -> Stream.of(s);
                    case ScheduledList f -> Stream.of(f.value());
                    default -> Stream.of();
                }).toList();
                for (Scheduled scheduled : scheduledList) {
                    if (method.parameters().length != 0) {
                        ScxApp.logger.log(ERROR,
                                "被 Scheduled 注解标识的方法不可以有参数 Class [{0}] , Method [{1}]",
                                classInfo.name(),
                                method.name()
                        );
                        break;
                    }
                    if (method.isStatic()) {
                        ScxScheduling.cron()
                                .expression(scheduled.cron())
                                .start(c -> {
                                    try {
                                        method.invoke(null);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    } else {
                        ScxScheduling.cron()
                                .expression(scheduled.cron())
                                .start(c -> {
                                    try {
                                        method.invoke(bean);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    }
                }

            }
        }
    }

    /// 数据源连接异常
    ///
    /// @param e a [java.lang.Exception] object.
    static void dataSourceExceptionHandler(Exception e) {
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
            var result = System.console().readLine().trim();
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

    static void initScxLoggerFactory(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        initScxLoggerFactory0(scxConfig, scxEnvironment);
        scxConfig.onChange((oldValue, newValue) -> {
            initScxLoggerFactory0(scxConfig, scxEnvironment);
        });
    }

    /// a
    ///
    /// @param scxConfig      a
    /// @param scxEnvironment a
    static void initScxLoggerFactory0(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        //先初始化好 DefaultScxLoggerInfo
        var defaultLevel = toLevel(scxConfig.get("scx.logging.default.level", String.class));
        var defaultType = toType(scxConfig.get("scx.logging.default.type", String.class));
        var defaultStoredDirectory = scxConfig.get("scx.logging.default.stored-directory", AppRootHandler.of(scxEnvironment, "AppRoot:logs"));
        var defaultStackTrace = scxConfig.get("scx.logging.default.stack-trace", DefaultValueHandler.of(false));

        //设置默认的 config 这里我们先清除所有的 Recorders
        var defaultConfig = ScxLogging.rootConfig().clearRecorders();
        defaultConfig.setLevel(defaultLevel);
        if (defaultType == LoggingType.CONSOLE || defaultType == LoggingType.BOTH) {
            defaultConfig.addRecorder(new ConsoleRecorder());
        }
        if (defaultType == LoggingType.FILE || defaultType == LoggingType.BOTH) {
            defaultConfig.addRecorder(new FileRecorder(defaultStoredDirectory));
        }
        defaultConfig.setStackTrace(defaultStackTrace);

        //以下日志若有缺少的 storedDirectory 则全部以 defaultStoredDirectory 为准
        var loggers = scxConfig.get("scx.logging.loggers", ConvertValueHandler.of(new TypeReference<List<Map<String, String>>>() {
        }));
        if (loggers != null) {
            for (var logger : loggers) {
                var name = logger.get("name");
                if (StringUtils.notBlank(name)) {
                    var level = toLevel(logger.get("level"));
                    var type = toType(logger.get("type"));
                    var storedDirectory = StringUtils.notBlank(logger.get("stored-directory")) ? scxEnvironment.getPathByAppRoot(logger.get("stored-directory")) : null;
                    var stackTrace = ScxObject.convertValue(logger.get("stack-trace"), Boolean.class);
                    var config = new ScxLoggerConfig();
                    config.setLevel(level);
                    if (type == LoggingType.CONSOLE || type == LoggingType.BOTH) {
                        config.addRecorder(new ConsoleRecorder());
                    }
                    if (type == LoggingType.FILE || type == LoggingType.BOTH) {
                        //文件路径的缺省值使用 默认的
                        config.addRecorder(new FileRecorder(storedDirectory != null ? storedDirectory : defaultStoredDirectory));
                    }
                    config.setStackTrace(stackTrace);
                    ScxLogging.setConfig(name, config);
                }
            }
        }
    }

    private static System.Logger.Level toLevel(String levelName) {
        Objects.requireNonNull(levelName, "levelName 不能为空 !!!");
        var s = levelName.trim().toUpperCase();
        return switch (s) {
            case "OFF", "O" -> OFF;
            case "ERROR", "E" -> ERROR;
            case "WARN", "WARNING", "W" -> WARNING;
            case "INFO", "I" -> INFO;
            case "DEBUG", "D" -> DEBUG;
            case "TRACE", "T" -> TRACE;
            case "ALL", "A" -> ALL;
            default -> null;
        };
    }

    private static LoggingType toType(String loggingTypeName) {
        requireNonNull(loggingTypeName, "loggingTypeName 不能为空 !!!");
        var s = loggingTypeName.trim().toUpperCase();
        return switch (s) {
            case "CONSOLE", "C" -> LoggingType.CONSOLE;
            case "FILE", "F" -> LoggingType.FILE;
            case "BOTH", "B" -> LoggingType.BOTH;
            default -> null;
        };
    }

    private enum LoggingType {

        /// 打印到控制台
        CONSOLE,

        /// 写入到文件
        FILE,

        /// 既打印到控制台也同时写入到文件
        BOTH
    }

}
