package cool.scx.ffm.platform.win32.type;

import cool.scx.ffm.type.callback.Callback;

import java.lang.foreign.MemorySegment;

public interface WNDENUMPROC extends Callback {

    boolean callback(MemorySegment hwnd, long lParam);

}
