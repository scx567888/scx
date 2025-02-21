package cool.scx.ansi;

import cool.scx.common.os.OSHelper;
import cool.scx.ffm.type.mapper.IntMapper;

import static cool.scx.common.os.OSType.WINDOWS;
import static cool.scx.ffm.platform.win32.Kernel32.KERNEL32;
import static cool.scx.ffm.platform.win32.WinBase.STD_OUTPUT_HANDLE;
import static cool.scx.ffm.platform.win32.WinCon.ENABLE_VIRTUAL_TERMINAL_PROCESSING;

/// AnsiHelper
///
/// @author scx567888
/// @version 0.0.1
class AnsiHelper {

    /// Windows 10 支持 Ansi 但是默认并没有开启, 这个方法用来开启 Windows 10 的 Ansi 支持
    static void enableWindows10AnsiSupport() {
        // 获取 标准输出设备句柄
        var hOut = KERNEL32.GetStdHandle(STD_OUTPUT_HANDLE);

        // 获取 当前输出模式
        var lpModeMapper = new IntMapper();
        KERNEL32.GetConsoleMode(hOut, lpModeMapper);

        //设置 标准输出设备 支持 Ansi, 这里按位或操作 防止覆盖原有模式
        KERNEL32.SetConsoleMode(hOut, lpModeMapper.getValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
    }

    /// 检测是否支持 ansi
    ///
    /// @return ansi
    static boolean detectIfAnsiCapable() {
        var osInfo = OSHelper.getOSInfo();

        // 不是 Windows 表示支持
        if (osInfo.type() != WINDOWS) {
            return true;
        }

        // 不是 Windows 10 以上则表示不支持
        if (!osInfo.version().startsWith("10") && !osInfo.version().startsWith("11")) {
            return false;
        }

        //尝试启用 Windows 10 Ansi 支持
        try {
            enableWindows10AnsiSupport();
            return true;
        } catch (Exception e) {
            // 如果开启失败 则表示不支持
            return false;
        }

    }

}
