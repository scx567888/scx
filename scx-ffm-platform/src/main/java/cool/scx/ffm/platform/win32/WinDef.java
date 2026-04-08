package cool.scx.ffm.platform.win32;

import cool.scx.ffm.type.Struct;

public final class WinDef {

    /// GetWindowRect 结构
    public static class RECT implements Struct {

        public int left;
        public int top;
        public int right;
        public int bottom;

    }

}
