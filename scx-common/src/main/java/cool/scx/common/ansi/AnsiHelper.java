package cool.scx.common.ansi;

import com.sun.jna.Function;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import cool.scx.common.util.OSHelper;

import static cool.scx.common.standard.OSType.WINDOWS;

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

        // See https://docs.microsoft.com/zh-cn/windows/console/getstdhandle
        var GetStdHandleFunc = Function.getFunction("kernel32", "GetStdHandle");
        var STD_OUTPUT_HANDLE = new WinDef.DWORD(-11);
        var hOut = (WinNT.HANDLE) GetStdHandleFunc.invoke(WinNT.HANDLE.class, new Object[]{STD_OUTPUT_HANDLE});

        //See https://docs.microsoft.com/zh-cn/windows/console/getconsolemode
        var p_dwMode = new WinDef.DWORDByReference(new WinDef.DWORD(0));
        var GetConsoleModeFunc = Function.getFunction("kernel32", "GetConsoleMode");
        GetConsoleModeFunc.invoke(WinDef.BOOL.class, new Object[]{hOut, p_dwMode});

        //See https://docs.microsoft.com/zh-cn/windows/console/setconsolemode
        int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;
        var dwMode = p_dwMode.getValue();
        dwMode.setValue(dwMode.intValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
        var SetConsoleModeFunc = Function.getFunction("kernel32", "SetConsoleMode");
        SetConsoleModeFunc.invoke(WinDef.BOOL.class, new Object[]{hOut, dwMode});

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
