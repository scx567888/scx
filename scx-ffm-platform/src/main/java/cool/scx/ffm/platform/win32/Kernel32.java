package cool.scx.ffm.platform.win32;

import cool.scx.ffm.ScxFFM;
import cool.scx.ffm.mapper.IntMapper;

import java.lang.foreign.MemorySegment;

/// 提供一些 Kernel32 标准的接口
///
/// @author scx567888
/// @version 0.0.1
public interface Kernel32 {

    Kernel32 KERNEL32 = ScxFFM.ffmProxy("kernel32", Kernel32.class);

    // https://learn.microsoft.com/zh-cn/windows/console/getstdhandle
    MemorySegment GetStdHandle(int nStdHandle);

    // https://learn.microsoft.com/zh-cn/windows/console/getconsolemode
    boolean GetConsoleMode(MemorySegment hConsoleHandle, IntMapper lpMode);

    // https://learn.microsoft.com/zh-cn/windows/console/setconsolemode
    boolean SetConsoleMode(MemorySegment hConsoleHandle, long dwMode);

}
