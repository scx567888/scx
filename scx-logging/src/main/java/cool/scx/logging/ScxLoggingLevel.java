package cool.scx.logging;

import java.util.Objects;

/**
 * ScxLoggingLevel 日志级别
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum ScxLoggingLevel {

    /**
     * OFF
     */
    OFF(0),

    /**
     * FATAL
     */
    FATAL(10),

    /**
     * ERROR
     */
    ERROR(20),

    /**
     * WARN
     */
    WARN(30),

    /**
     * INFO
     */
    INFO(40),

    /**
     * DEBUG
     */
    DEBUG(50),

    /**
     * TRACE
     */
    TRACE(60),

    /**
     * ALL
     */
    ALL(70);

    /**
     * int 级别
     */
    private final int levelInt;

    /**
     * 定长名称 (5位), 不足的补齐空格 方便打印时更好的格式化
     */
    private final String fixedLengthName;

    /**
     * <p>Constructor for ScxLoggingLevel.</p>
     *
     * @param i a int
     */
    ScxLoggingLevel(int i) {
        this.levelInt = i;
        this.fixedLengthName = (name() + "  ").substring(0, 5);
    }

    /**
     * 根据名称获取  ScxLoggingLevel 可使用简写
     *
     * @param levelName 名称 可选值 [OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL]  以及对应的简写形式 [O, F, E, W, I, D, T, A]
     * @return ScxLoggingLevel
     */
    public static ScxLoggingLevel of(String levelName) {
        Objects.requireNonNull(levelName, "levelName 不能为空 !!!");
        var s = levelName.trim().toUpperCase();
        return switch (s) {
            case "OFF", "O" -> OFF;
            case "FATAL", "F" -> FATAL;
            case "ERROR", "E" -> ERROR;
            case "WARN", "W" -> WARN;
            case "INFO", "I" -> INFO;
            case "DEBUG", "D" -> DEBUG;
            case "TRACE", "T" -> TRACE;
            case "ALL", "A" -> ALL;
            default -> throw new IllegalArgumentException("levelName 值不合法 :" + levelName);
        };
    }

    /**
     * 根据名称获取  ScxLoggingLevel 可使用简写 失败则采用默认值
     *
     * @param levelName    名称
     * @param defaultLevel 默认的级别
     * @return ScxLoggingLevel
     */
    public static ScxLoggingLevel of(String levelName, ScxLoggingLevel defaultLevel) {
        try {
            return of(levelName);
        } catch (Exception e) {
            return defaultLevel;
        }
    }

    /**
     * levelInt
     *
     * @return a
     */
    public int toInt() {
        return levelInt;
    }

    /**
     * 定长名称 (5位), 不足的补齐空格 方便打印时更好的格式化
     *
     * @return a
     */
    public String fixedLengthName() {
        return fixedLengthName;
    }

}
