package cool.scx.common.ffm.platform.win32.type;

import cool.scx.common.ffm.Callback;

import java.lang.foreign.MemorySegment;

public interface WNDENUMPROC extends Callback {

    boolean callback(MemorySegment hwnd, long lParam);

}
