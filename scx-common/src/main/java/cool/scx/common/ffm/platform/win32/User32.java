package cool.scx.common.ffm.platform.win32;


import cool.scx.common.ffm.platform.win32.type.RECT;
import cool.scx.common.ffm.platform.win32.type.WNDENUMPROC;

import java.lang.foreign.MemorySegment;

import static cool.scx.common.ffm.FFMProxy.ffmProxy;

public interface User32 {

    User32 USER32 = ffmProxy("user32", User32.class);

    // https://learn.microsoft.com/zh-cn/windows/win32/api/winuser/nf-winuser-messageboxa
    int MessageBoxA(MemorySegment hWnd, String lpText, String lpCaption, int uType);

    // https://learn.microsoft.com/zh-cn/windows/win32/api/winuser/nf-winuser-iswindowvisible
    boolean IsWindowVisible(MemorySegment hWnd);

    MemorySegment GetParent(MemorySegment hWnd);

    int GetWindowTextLengthW(MemorySegment hWnd);

    int GetClassNameW(MemorySegment hWnd, char[] lpClassName, int nMaxCount);

    int GetWindowTextW(MemorySegment hWnd, char[] lpString, int nMaxCount);

    MemorySegment GetWindowThreadProcessId(MemorySegment hWnd, MemorySegment lpdwProcessId);

    boolean PostMessageW(MemorySegment hWnd, int Msg, MemorySegment wParam, MemorySegment lParam);

    boolean SendMessageW(MemorySegment hWnd, int Msg, MemorySegment wParam, MemorySegment lParam);

    boolean EnumWindows(WNDENUMPROC lpEnumFunc, long lParam);

    MemorySegment FindWindowA(String lpClassName, String lpWindowName);

    boolean CloseWindow(MemorySegment hWnd);

    boolean GetWindowRect(MemorySegment hWnd, RECT lpRect);

}
