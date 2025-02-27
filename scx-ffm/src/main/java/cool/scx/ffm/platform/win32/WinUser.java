package cool.scx.ffm.platform.win32;

import cool.scx.ffm.type.callback.Callback;

import java.lang.foreign.MemorySegment;

public final class WinUser {

    /// EnumWindows 回调接口
    public interface WNDENUMPROC extends Callback {

        boolean callback(MemorySegment hwnd, long lParam);

    }
    
}
