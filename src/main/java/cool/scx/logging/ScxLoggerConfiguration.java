package cool.scx.logging;

import com.fasterxml.jackson.core.type.TypeReference;
import cool.scx.ScxEnvironment;
import cool.scx.config.ScxConfig;
import cool.scx.config.handler.AppRootHandler;
import cool.scx.config.handler.ConvertValueHandler;
import cool.scx.config.handler.DefaultValueHandler;
import cool.scx.util.ObjectUtils;
import cool.scx.util.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * ScxLog 配置类
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class ScxLoggerConfiguration {

    /**
     * a
     */
    public static final DateTimeFormatter LOG_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * a
     */
    public static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    /**
     * a
     *
     * @param scxConfig      a
     * @param scxEnvironment a
     */
    public static void init(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        initDefault(scxConfig, scxEnvironment);
        initLoggers(scxConfig, scxEnvironment);
    }

    /**
     * <p>initDefault.</p>
     *
     * @param scxConfig      a {@link cool.scx.config.ScxConfig} object
     * @param scxEnvironment a {@link cool.scx.ScxEnvironment} object
     */
    private static void initDefault(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        //先初始化好 DefaultScxLoggerInfo
        var defaultLevel = ScxLoggingLevel.of(scxConfig.get("scx.logging.default.level", String.class), ScxLoggingLevel.ERROR);
        var defaultType = ScxLoggingType.of(scxConfig.get("scx.logging.default.type", String.class), ScxLoggingType.CONSOLE);
        var defaultStoredDirectory = scxConfig.get("scx.logging.default.stored-directory", new AppRootHandler("AppRoot:logs", scxEnvironment)).toPath();
        var defaultStackTrace = scxConfig.get("scx.logging.default.stack-trace", new DefaultValueHandler<>(false));
        ScxLoggerFactory.updateDefault(defaultLevel, defaultType, defaultStoredDirectory, defaultStackTrace);
    }

    /**
     * <p>initLoggers.</p>
     *
     * @param scxConfig      a {@link cool.scx.config.ScxConfig} object
     * @param scxEnvironment a {@link cool.scx.ScxEnvironment} object
     */
    private static void initLoggers(ScxConfig scxConfig, ScxEnvironment scxEnvironment) {
        //以下日志若有缺少的属性则全部以 defaultScxLoggerInfo 为准
        var loggers = scxConfig.get("scx.logging.loggers", new ConvertValueHandler<>(new TypeReference<List<Map<String, String>>>() {
        }));
        if (loggers != null) {
            for (var logger : loggers) {
                var name = logger.get("name");
                if (StringUtils.isNotBlank(name)) {
                    var level = ScxLoggingLevel.of(logger.get("level"), null);
                    var type = ScxLoggingType.of(logger.get("type"), null);
                    var storedDirectory = StringUtils.isNotBlank(logger.get("stored-directory")) ? scxEnvironment.getFileByAppRoot(logger.get("stored-directory")).toPath() : null;
                    var stackTrace = ObjectUtils.convertValue(logger.get("stack-trace"), Boolean.class);
                    ScxLoggerFactory.updateLogger(name, level, type, storedDirectory, stackTrace);
                }
            }
        }
    }

    /**
     * 是否为 日志 class 为了减少日志中噪声 我们把日志框架所属的类去除掉
     *
     * @param className className
     * @return a
     */
    private static boolean notLoggerClass(String className) {
        return !className.startsWith("cool.scx.logging")
                && !className.startsWith("org.slf4j.helpers")
                && !className.startsWith("org.apache.logging.log4j");
    }

    /**
     * a
     *
     * @param stringBuilder a
     */
    static void getStackTraceInfo(StringBuilder stringBuilder) {
        var frames = WALKER.walk((s) -> s.filter(b -> notLoggerClass(b.getClassName())).toList());
        for (var frame : frames) {
            stringBuilder.append("\t").append(frame).append(System.lineSeparator());
        }
    }

}
