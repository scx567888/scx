package cool.scx.ffm.platform.win32.type;

import cool.scx.ffm.type.callback.Callback;

import java.lang.foreign.MemorySegment;

/**
 * EnumWindows 回调接口
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface WNDENUMPROC extends Callback {

    boolean callback(MemorySegment hwnd, long lParam);

}
