package cool.scx.log;

import org.slf4j.event.Level;

import java.util.Objects;

/**
 * a
 */
public enum ScxLogLevel {

    /**
     * a
     */
    OFF(0, "OFF"),

    /**
     * a
     */
    FATAL(10, "FATAL"),

    /**
     * a
     */
    ERROR(20, "ERROR"),

    /**
     * a
     */
    WARN(30, "WARN"),

    /**
     * a
     */
    INFO(40, "INFO"),

    /**
     * a
     */
    DEBUG(50, "DEBUG"),

    /**
     * a
     */
    TRACE(60, "TRACE"),

    /**
     * a
     */
    ALL(70, "ALL");

    private final int levelInt;

    private final String levelStr;

    private final String fixedLengthStr;

    ScxLogLevel(int i, String s) {
        levelInt = i;
        levelStr = s;
        fixedLengthStr = (levelStr + "  ").substring(0, 5);
    }

    /**
     * a
     *
     * @param levelName a
     * @return a
     */
    public static ScxLogLevel of(String levelName) {
        Objects.requireNonNull(levelName, "levelName 不能为空 !!!");
        var finalLevelName = levelName.trim().toUpperCase();
        return switch (finalLevelName) {
            case "OFF" -> ScxLogLevel.OFF;
            case "FATAL" -> ScxLogLevel.FATAL;
            case "ERROR" -> ScxLogLevel.ERROR;
            case "WARN" -> ScxLogLevel.WARN;
            case "INFO" -> ScxLogLevel.INFO;
            case "DEBUG" -> ScxLogLevel.DEBUG;
            case "TRACE" -> ScxLogLevel.TRACE;
            case "ALL" -> ScxLogLevel.ALL;
            default -> throw new IllegalArgumentException("无法识别 ScxLogLevel [" + finalLevelName + "] !!!");
        };
    }

    /**
     * a
     *
     * @param levelName    a
     * @param defaultLevel a
     * @return a
     */
    public static ScxLogLevel of(String levelName, ScxLogLevel defaultLevel) {
        try {
            return of(levelName);
        } catch (Exception e) {
            return defaultLevel;
        }
    }

    /**
     * a
     *
     * @param slf4jLevel a
     * @return a
     */
    public static ScxLogLevel of(org.slf4j.event.Level slf4jLevel) {
        Objects.requireNonNull(slf4jLevel, "slf4jLevel 不能为空 !!!");
        return of(slf4jLevel.toString());
    }

    /**
     * a
     *
     * @param log4jLevel a
     * @return a
     */
    public static ScxLogLevel of(org.apache.logging.log4j.Level log4jLevel) {
        Objects.requireNonNull(log4jLevel, "log4jLevel 不能为空 !!!");
        return of(log4jLevel.name());
    }

    /**
     * a
     *
     * @return a
     */
    public int toInt() {
        return levelInt;
    }

    /**
     * a
     *
     * @return a
     */
    public String toFixedLengthString() {
        return fixedLengthStr;
    }

    @Override
    public String toString() {
        return levelStr;
    }

    /**
     * a
     *
     * @return a
     */
    public org.slf4j.event.Level toSLF4JLevel() {
        //因为 SLF4J 日志级别分类较少所以这里做一个处理
        if (this == OFF || this == FATAL) {
            return Level.ERROR;
        } else if (this == ALL) {
            return Level.TRACE;
        } else {
            return org.slf4j.event.Level.valueOf(this.levelStr);
        }
    }

    /**
     * a
     *
     * @return a
     */
    public org.apache.logging.log4j.Level toLog4jLevel() {
        return org.apache.logging.log4j.Level.getLevel(this.levelStr);
    }

}
