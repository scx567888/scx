package cool.scx.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cool.scx.common.util.ClassUtils;
import cool.scx.common.util.ConsoleUtils;
import cool.scx.common.util.ObjectUtils;
import cool.scx.common.util.StringUtils;
import cool.scx.config.ScxConfig;
import cool.scx.config.ScxEnvironment;
import cool.scx.config.ScxFeatureConfig;
import cool.scx.config.handler.AppRootHandler;
import cool.scx.config.handler.ConvertValueHandler;
import cool.scx.config.handler.DefaultValueHandler;
import cool.scx.core.annotation.Scheduled;
import cool.scx.core.annotation.ScheduledList;
import cool.scx.core.annotation.ScxService;
import cool.scx.core.base.BaseModel;
import cool.scx.core.base.BaseModelService;
import cool.scx.core.enumeration.ScxCoreFeature;
import cool.scx.data.jdbc.annotation.Table;
import cool.scx.jdbc.dialect.DialectSelector;
import cool.scx.jdbc.spy.Spy;
import cool.scx.logging.ScxLoggerConfig;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.recorder.ConsoleRecorder;
import cool.scx.logging.recorder.FileRecorder;
import cool.scx.reflect.ReflectFactory;
import cool.scx.scheduling.ScxScheduling;
import cool.scx.web.annotation.ScxRoute;
import cool.scx.web.annotation.ScxWebSocketRoute;
import io.helidon.common.tls.Tls;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static cool.scx.common.util.ClassUtils.*;
import static cool.scx.reflect.AccessModifier.PUBLIC;
import static java.lang.System.Logger.Level.*;
import static java.util.Objects.requireNonNull;

/**
 * <p>ScxHelper class.</p>
 *
 * @author scx567888
 * @version 1.16.4
 */
public final class ScxHelper {

    /**
     * Constant <code>beanFilterAnnotation</code>
     */
    private static final List<Class<? extends Annotation>> beanFilterAnnotation = List.of(
            //scx 注解
            ScxRoute.class, Table.class, ScxService.class, ScxWebSocketRoute.class);

    static Path findRootPathByScxModule(Class<? extends ScxModule> c) throws IOException {
        var classSource = getCodeSource(c);
        var classSourcePath = Path.of(classSource);
        var isJar = isJar(classSourcePath);
        //判断当前是否处于 jar 包中 并使用不同的 方式加载
        return isJar ? classSourcePath.getParent() : classSourcePath;
    }

    /**
     * 根据 ScxModule 的 class 查找 所有 class
     *
     * @param c c
     * @return class 列表 (注意这里返回的是不可变的列表 !!!)
     * @throws IOException r
     */
    static List<Class<?>> findClassListByScxModule(Class<? extends ScxModule> c) throws IOException {
        var classSource = getCodeSource(c);
        var classSourcePath = Path.of(classSource);
        var isJar = isJar(classSourcePath);
        //判断当前是否处于 jar 包中 并使用不同的 方式加载
        var allClassList = isJar ? findClassListFromJar(classSource) : findClassListFromPath(classSourcePath, c.getClassLoader());
        //使用 basePackage 过滤
        var basePackage = c.getPackageName();
        return List.of(filterByBasePackage(allClassList, basePackage));
    }

    /**
     * 拥有 scx 注解
     *
     * @param clazz class
     * @return b
     */
    public static boolean isBeanClass(Class<?> clazz) {
        for (var a : beanFilterAnnotation) {
            if (clazz.getAnnotation(a) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化 ScxModelClassList
     *
     * @param c a
     * @return a
     */
    public static boolean isScxBaseModelClass(Class<?> c) {
        return c.isAnnotationPresent(Table.class) &&  // 拥有注解
               ClassUtils.isInstantiableClass(c) &&  // 是一个可以不需要其他参数直接生成实例化的对象
               BaseModel.class.isAssignableFrom(c);
    }

    /**
     * <p>isScxBaseModelServiceClass.</p>
     *
     * @param c a {@link java.lang.Class} object
     * @return a boolean
     */
    public static boolean isScxBaseModelServiceClass(Class<?> c) {
        return c.isAnnotationPresent(ScxService.class) &&  // 拥有注解
               ClassUtils.isNormalClass(c) && // 是一个普通的类 (不是接口, 不是抽象类) ; 此处不要求有必须有无参构造函数 因为此类的创建会由 beanFactory 进行处理
               c.getGenericSuperclass() instanceof ParameterizedType t && //需要有泛型参数
               t.getActualTypeArguments().length == 1; //并且泛型参数的数量必须是一个
    }

    @SuppressWarnings("unchecked")
    public static <Entity extends BaseModel> Class<Entity> findBaseModelServiceEntityClass(Class<?> baseModelServiceClass) {
        var superClass = ReflectFactory.getClassInfo(baseModelServiceClass).findSuperType(BaseModelService.class);
        if (superClass != null) {
            var boundType = superClass.type().getBindings().getBoundType(0);
            if (boundType != null) {
                return (Class<Entity>) boundType.getRawClass();
            } else {
                throw new IllegalArgumentException(baseModelServiceClass.getName() + " : 必须设置泛型参数 !!!");
            }
        } else {
            throw new IllegalArgumentException(baseModelServiceClass.getName() + " : 必须继承自 BaseModelService !!!");
        }
    }

    /**
     * 获取新的可用的端口号 (使用弹窗让用户进行选择)
     *
     * @param port a
     * @return a
     */
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

    static DataSource initDataSource(ScxOptions scxOptions, ScxFeatureConfig scxFeatureConfig) {
        var url = scxOptions.dataSourceUrl();
        var dialect = DialectSelector.findDialect(url);
        var realDataSource = dialect.createDataSource(url, scxOptions.dataSourceUsername(), scxOptions.dataSourcePassword(), scxOptions.dataSourceParameters());
        //使用 HikariDataSource 进行包装
        var hikariConfig = new HikariConfig();
        hikariConfig.setDataSource(realDataSource);
        var hikariDataSource = new HikariDataSource(hikariConfig);
        return scxFeatureConfig.get(ScxCoreFeature.USE_SPY) ? Spy.wrap(hikariDataSource) : hikariDataSource;
    }

    static ScxModule[] initScxModuleMetadataList(ScxModule[] scxModules) {
        //2, 检查模块参数是否正确
        if (scxModules == null || Arrays.stream(scxModules).noneMatch(Objects::nonNull)) {
            throw new IllegalArgumentException("Modules must not be empty !!!");
        }
        return scxModules;
    }

    static DefaultListableBeanFactory initBeanFactory(ScxModule[] modules, ScxFeatureConfig scxFeatureConfig) {
        var beanFactory = new DefaultListableBeanFactory();
        //这里添加一个 bean 的后置处理器以便可以使用 @Autowired 注解
        var beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        beanPostProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(beanPostProcessor);
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
            beanFactory.registerBeanDefinition(c.getName(), beanDefinition);
        }
        return beanFactory;
    }

    public static void startAnnotationScheduled(DefaultListableBeanFactory beanFactory) {
        var beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (var beanDefinitionName : beanDefinitionNames) {
            var bean = beanFactory.getBean(beanDefinitionName);
            var classInfo = ReflectFactory.getClassInfo(bean.getClass());
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
                        Scx.logger.log(ERROR,
                                "被 Scheduled 注解标识的方法不可以有参数 Class [{0}] , Method [{1}]",
                                classInfo.type().getRawClass().getName(),
                                method.name()
                        );
                        break;
                    }
                    if (method.isStatic()) {
                        ScxScheduling.cron()
                                .expression(scheduled.cron())
                                .start(c -> {
                                    try {
                                        method.method().invoke(null);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    } else {
                        ScxScheduling.cron()
                                .expression(scheduled.cron())
                                .start(c -> {
                                    try {
                                        method.method().invoke(bean);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    }
                }
             
            }
        }
    }

    /**
     * 数据源连接异常
     *
     * @param e a {@link java.lang.Exception} object.
     */
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
     * a
     *
     * @param scxConfig      a
     * @param scxEnvironment a
     */
    static void initScxLoggerFactory(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        //先初始化好 DefaultScxLoggerInfo
        var defaultLevel = toLevel(scxConfig.get("scx.logging.default.level", String.class));
        var defaultType = toType(scxConfig.get("scx.logging.default.type", String.class));
        var defaultStoredDirectory = scxConfig.get("scx.logging.default.stored-directory", AppRootHandler.of(scxEnvironment, "AppRoot:logs"));
        var defaultStackTrace = scxConfig.get("scx.logging.default.stack-trace", DefaultValueHandler.of(false));

        //设置默认的 config 这里我们先清除所有的 Recorders
        var defaultConfig = ScxLoggerFactory.rootConfig().clearRecorders();
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
                    var stackTrace = ObjectUtils.convertValue(logger.get("stack-trace"), Boolean.class);
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
                    ScxLoggerFactory.setConfig(name, config);
                }
            }
        }
    }

    /**
     * <p>toLevel.</p>
     *
     * @param levelName a {@link java.lang.String} object
     * @return a
     */
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

    public static Tls getTls(Path path, String password) {
        var builder = Tls.builder();
        try {
            var jks = KeyStore.getInstance(path.toFile(), password.toCharArray());
            var aliases = jks.aliases();
            if (aliases.hasMoreElements()) {
                var a = aliases.nextElement();
                var key = jks.getKey(a, password.toCharArray());
                if (key instanceof PrivateKey p) {
                    builder.privateKey(p);
                }
                var c = jks.getCertificate(a);
                if (c instanceof X509Certificate x) {
                    builder.addPrivateKeyCertChain(x);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("加载 SSL 证书时发生错误 !!!", e);
        }
        return builder.build();
    }

    private enum LoggingType {

        /**
         * 打印到控制台
         */
        CONSOLE,

        /**
         * 写入到文件
         */
        FILE,

        /**
         * 既打印到控制台也同时写入到文件
         */
        BOTH
    }

}
