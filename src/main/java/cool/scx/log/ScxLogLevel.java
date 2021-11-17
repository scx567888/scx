package cool.scx.log;

import org.slf4j.event.Level;

import java.util.Objects;

public enum ScxLogLevel {

    OFF(0, "OFF"),
    FATAL(10, "FATAL"),
    ERROR(20, "ERROR"),
    WARN(30, "WARN"),
    INFO(40, "INFO"),
    DEBUG(50, "DEBUG"),
    TRACE(60, "TRACE"),
    ALL(70, "ALL");

    private final int levelInt;

    private final String levelStr;

    private final String fixedLengthStr;

    ScxLogLevel(int i, String s) {
        levelInt = i;
        levelStr = s;
        fixedLengthStr = (levelStr + "  ").substring(0, 5);
    }

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

    public static ScxLogLevel of(String levelName, ScxLogLevel defaultLevel) {
        try {
            return of(levelName);
        } catch (Exception e) {
            return defaultLevel;
        }
    }

    public static ScxLogLevel of(org.slf4j.event.Level slf4jLevel) {
        Objects.requireNonNull(slf4jLevel, "slf4jLevel 不能为空 !!!");
        return of(slf4jLevel.toString());
    }

    public static ScxLogLevel of(org.apache.logging.log4j.Level log4jLevel) {
        Objects.requireNonNull(log4jLevel, "log4jLevel 不能为空 !!!");
        return of(log4jLevel.name());
    }

    public int toInt() {
        return levelInt;
    }

    public String toFixedLengthString() {
        return fixedLengthStr;
    }

    @Override
    public String toString() {
        return levelStr;
    }

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

    public org.apache.logging.log4j.Level toLog4jLevel() {
        return org.apache.logging.log4j.Level.getLevel(this.levelStr);
    }

}
