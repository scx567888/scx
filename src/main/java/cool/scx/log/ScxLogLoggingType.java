package cool.scx.log;

import java.util.Objects;

public enum ScxLogLoggingType {
    CONSOLE,
    FILE,
    BOTH;

    public static ScxLogLoggingType of(String loggingTypeName, ScxLogLoggingType defaultLoggingType) {
        try {
            return of(loggingTypeName);
        } catch (Exception e) {
            return defaultLoggingType;
        }
    }

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