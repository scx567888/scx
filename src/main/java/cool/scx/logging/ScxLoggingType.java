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
        return ScxLoggingType.valueOf(loggingTypeName.trim().toUpperCase());
    }

}