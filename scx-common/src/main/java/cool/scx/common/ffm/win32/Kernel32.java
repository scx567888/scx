package cool.scx.common.ffm.win32;

import cool.scx.common.ffm.type.IntRef;

import java.lang.foreign.MemorySegment;

import static cool.scx.common.ffm.FFMProxy.ffmProxy;

public interface Kernel32 {

    Kernel32 KERNEL32 = ffmProxy("kernel32", Kernel32.class);

    // https://learn.microsoft.com/zh-cn/windows/console/getstdhandle
    MemorySegment GetStdHandle(int nStdHandle);

    // https://learn.microsoft.com/zh-cn/windows/console/getconsolemode
    boolean GetConsoleMode(MemorySegment hConsoleHandle, IntRef lpMode);

    // https://learn.microsoft.com/zh-cn/windows/console/setconsolemode
    boolean SetConsoleMode(MemorySegment hConsoleHandle, long dwMode);

}
