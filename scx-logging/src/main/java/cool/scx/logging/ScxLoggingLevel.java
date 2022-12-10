package cool.scx.logging;

import java.util.Objects;

/**
 * a
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum ScxLoggingLevel {

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

    /**
     * a
     */
    private final int levelInt;

    /**
     * a
     */
    private final String levelStr;

    /**
     * a
     */
    private final String fixedLengthStr;

    /**
     * <p>Constructor for ScxLoggingLevel.</p>
     *
     * @param i a int
     * @param s a {@link String} object
     */
    ScxLoggingLevel(int i, String s) {
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
    public static ScxLoggingLevel of(String levelName) {
        Objects.requireNonNull(levelName, "levelName 不能为空 !!!");
        return ScxLoggingLevel.valueOf(levelName.trim().toUpperCase());
    }

    /**
     * a
     *
     * @param levelName    a
     * @param defaultLevel a
     * @return a
     */
    public static ScxLoggingLevel of(String levelName, ScxLoggingLevel defaultLevel) {
        try {
            return of(levelName);
        } catch (Exception e) {
            return defaultLevel;
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return levelStr;
    }

}
