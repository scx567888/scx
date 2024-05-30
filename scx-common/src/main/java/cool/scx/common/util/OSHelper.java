package cool.scx.common.util;

import cool.scx.common.standard.OSType;

import static cool.scx.common.standard.OSType.*;

/**
 * OSHelper 用来获取操作系统信息
 */
public final class OSHelper {

    private static final OSInfo osInfo = initOSInfo();

    private static OSInfo initOSInfo() {
        var osType = getOSType();
        var osVersion = System.getProperty("os.version");
        return new OSInfo(osType, osVersion);
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

    public record OSInfo(OSType type, String version) {

    }

}
