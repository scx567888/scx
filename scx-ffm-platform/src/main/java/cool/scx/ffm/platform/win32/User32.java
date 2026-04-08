package cool.scx.ffm.platform.win32;

import cool.scx.ffm.ScxFFM;
import cool.scx.ffm.platform.win32.WinDef.RECT;
import cool.scx.ffm.platform.win32.WinUser.WNDENUMPROC;

import java.lang.foreign.MemorySegment;

import static cool.scx.ffm.platform.win32.WinUser.POINT;

/// 提供一些 User32 标准的接口
///
/// @author scx567888
/// @version 0.0.1
public interface User32 {

    User32 USER32 = ScxFFM.ffmProxy("user32", User32.class);

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

    boolean SetCursorPos(int x, int y);

    boolean GetCursorPos(POINT lpPoint);

}
