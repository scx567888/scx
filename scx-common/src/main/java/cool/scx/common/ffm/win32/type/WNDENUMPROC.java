package cool.scx.common.ffm.win32.type;

import cool.scx.common.ffm.type.Callback;

import java.lang.foreign.MemorySegment;

public interface WNDENUMPROC extends Callback {

    boolean callback(MemorySegment hwnd, long lParam);

}
