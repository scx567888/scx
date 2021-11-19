package cool.scx.log;

import java.util.Objects;

/**
 * a
 */
public enum ScxLogLoggingType {

    /**
     * a
     */
    CONSOLE,

    /**
     * a
     */
    FILE,

    /**
     * a
     */
    BOTH;

    /**
     * a
     *
     * @param loggingTypeName    a
     * @param defaultLoggingType a
     * @return a
     */
    public static ScxLogLoggingType of(String loggingTypeName, ScxLogLoggingType defaultLoggingType) {
        try {
            return of(loggingTypeName);
        } catch (Exception e) {
            return defaultLoggingType;
        }
    }

    /**
     * a
     *
     * @param loggingTypeName a
     * @return a
     */
    public static ScxLogLoggingType of(String loggingTypeName) {
        Objects.requireNonNull(loggingTypeName, "loggingTypeName 不能为空 !!!");
        var finalLoggingTypeName = loggingTypeName.trim().toUpperCase();
        return switch (finalLoggingTypeName) {
            case "CONSOLE" -> ScxLogLoggingType.CONSOLE;
            case "FILE" -> ScxLogLoggingType.FILE;
            case "BOTH" -> ScxLogLoggingType.BOTH;
            default -> throw new IllegalArgumentException("无法识别 ScxLogLoggingType [" + finalLoggingTypeName + "] !!!");
        };
    }

}