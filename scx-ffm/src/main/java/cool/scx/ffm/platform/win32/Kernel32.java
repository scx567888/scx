package cool.scx.ffm.platform.win32;

import cool.scx.ffm.type.mapper.IntMapper;

import java.lang.foreign.MemorySegment;

import static cool.scx.ffm.FFMProxy.ffmProxy;

public interface Kernel32 {

    Kernel32 KERNEL32 = ffmProxy("kernel32", Kernel32.class);

    // https://learn.microsoft.com/zh-cn/windows/console/getstdhandle
    MemorySegment GetStdHandle(int nStdHandle);

    // https://learn.microsoft.com/zh-cn/windows/console/getconsolemode
    boolean GetConsoleMode(MemorySegment hConsoleHandle, IntMapper lpMode);

    // https://learn.microsoft.com/zh-cn/windows/console/setconsolemode
    boolean SetConsoleMode(MemorySegment hConsoleHandle, long dwMode);

}
