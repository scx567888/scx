package cool.scx.logging;

import static java.util.Objects.requireNonNull;

/**
 * ScxLogging 的记录类型
 *
 * @author scx567888
 * @version 0.0.1
 */
public enum ScxLoggingType {

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
    BOTH;

    /**
     * 根据 名称 获取 ScxLoggingType
     *
     * @param loggingTypeName    名称
     * @param defaultLoggingType 默认值
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
     * 根据 名称 获取 ScxLoggingType
     *
     * @param loggingTypeName 名称 可选值 [CONSOLE, FILE, BOTH] 以及对应的简写形式  [C, F, B]
     * @return ScxLoggingType
     */
    public static ScxLoggingType of(String loggingTypeName) {
        requireNonNull(loggingTypeName, "loggingTypeName 不能为空 !!!");
        var s = loggingTypeName.trim().toUpperCase();
        return switch (s) {
            case "CONSOLE", "C" -> CONSOLE;
            case "FILE", "F" -> FILE;
            case "B", "BOTH" -> BOTH;
            default -> throw new IllegalArgumentException("loggingTypeName 值不合法 :" + loggingTypeName);
        };
    }

}
