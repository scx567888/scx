package cool.scx.ansi;

import cool.scx.common.util.OSHelper;
import cool.scx.ffm.type.mapper.IntMapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static cool.scx.common.util.OSHelper.OSType.WINDOWS;
import static cool.scx.ffm.platform.win32.Kernel32.KERNEL32;

class AnsiHelper {

    /**
     * Windows 10 supports Ansi codes. However, it's still experimental and not enabled by default.
     * <br>
     * This method enables the necessary Windows 10 feature.
     * <br>
     * More info: <a href="https://stackoverflow.com/a/51681675/675577">...</a>
     * Code source: <a href="https://stackoverflow.com/a/52767586/675577">...</a>
     * Reported issue: <a href="https://github.com/PowerShell/PowerShell/issues/11449#issuecomment-569531747">...</a>
     */
    static void enableWindows10AnsiSupport() {

        // See https://learn.microsoft.com/zh-cn/windows/console/getstdhandle
        var hOut = KERNEL32.GetStdHandle(-11);

        // See https://learn.microsoft.com/zh-cn/windows/console/getconsolemode
        var lpModeMemorySegment = new IntMapper(0);
        KERNEL32.GetConsoleMode(hOut, lpModeMemorySegment);

        // See https://learn.microsoft.com/zh-cn/windows/console/setconsolemode
        int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;

        long lpMode = lpModeMemorySegment.getValue();
        long dwMode = lpMode | ENABLE_VIRTUAL_TERMINAL_PROCESSING;
        KERNEL32.SetConsoleMode(hOut, dwMode);

    }

    /**
     * 检测是否支持 ansi
     *
     * @return ansi
     */
    static boolean detectIfAnsiCapable() {
        var osInfo = OSHelper.getOSInfo();
        if (osInfo.type() == WINDOWS) {
            if (osInfo.version().startsWith("10") || osInfo.version().startsWith("11")) {
                try {
                    enableWindows10AnsiSupport();
                    return true;
                } catch (Exception e) {
                    // 如果开启失败 则表示不支持
                    return false;
                }
            } else {// 不是 Windows 10 以上则表示不支持
                return false;
            }
        } else {// 不是 Windows 表示支持
            return true;
        }
    }

}
