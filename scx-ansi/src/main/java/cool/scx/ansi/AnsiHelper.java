package cool.scx.ansi;

import cool.scx.common.os.OSHelper;
import cool.scx.ffm.type.mapper.IntMapper;

import static cool.scx.common.os.OSType.WINDOWS;
import static cool.scx.ffm.platform.win32.Kernel32.KERNEL32;
import static cool.scx.ffm.platform.win32.WinCon.ENABLE_VIRTUAL_TERMINAL_PROCESSING;

/**
 * AnsiHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
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
        // 获取 标准输出设备句柄
        var hOut = KERNEL32.GetStdHandle(-11);

        // 获取 当前输出模式
        var lpModeMapper = new IntMapper();
        KERNEL32.GetConsoleMode(hOut, lpModeMapper);

        //设置 标准输出设备 支持 Ansi, 通过按位或操作 防止覆盖原有模式
        KERNEL32.SetConsoleMode(hOut, lpModeMapper.getValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
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
