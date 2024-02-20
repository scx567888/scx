package cool.scx.util;

import static cool.scx.util.OSHelper.OSType.*;

public final class OSHelper {

    private static final OSInfo osInfo;

    static {
        var osType = getOSType();
        var osVersion = System.getProperty("os.version");
        osInfo = new OSInfo(osType, osVersion);
    }

    private static OSType getOSType() {
        var osName = System.getProperty("os.name");

        if (osName.startsWith("Linux")) {
            if ("dalvik".equalsIgnoreCase(System.getProperty("java.vm.name"))) {
                return ANDROID;
            } else {
                return LINUX;
            }
        } else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
            return MAC;
        } else if (osName.startsWith("Windows")) {
            return WINDOWS;
        } else {
            return UNKNOWN;
        }
    }

    public static OSInfo getOSInfo() {
        return osInfo;
    }

    public static boolean isMac() {
        return osInfo.type == MAC;
    }

    public static boolean isAndroid() {
        return osInfo.type == ANDROID;
    }

    public static boolean isLinux() {
        return osInfo.type == LINUX;
    }

    public static boolean isWindows() {
        return osInfo.type == WINDOWS;
    }

    public enum OSType {
        MAC,
        LINUX,
        WINDOWS,
        ANDROID,
        UNKNOWN
    }

    public record OSInfo(OSType type, String version) {

    }

}
