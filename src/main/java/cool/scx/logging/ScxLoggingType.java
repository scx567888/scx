package cool.scx.logging;

import java.util.Objects;

/**
 * a
 */
public enum ScxLoggingType {

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
    public static ScxLoggingType of(String loggingTypeName, ScxLoggingType defaultLoggingType) {
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
    public static ScxLoggingType of(String loggingTypeName) {
        Objects.requireNonNull(loggingTypeName, "loggingTypeName 不能为空 !!!");
        var finalLoggingTypeName = loggingTypeName.trim().toUpperCase();
        return switch (finalLoggingTypeName) {
            case "CONSOLE" -> ScxLoggingType.CONSOLE;
            case "FILE" -> ScxLoggingType.FILE;
            case "BOTH" -> ScxLoggingType.BOTH;
            default -> throw new IllegalArgumentException("无法识别 ScLoggingType [" + finalLoggingTypeName + "] !!!");
        };
    }

}