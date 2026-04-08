package cool.scx.ffm.platform.win32;

import cool.scx.ffm.type.Struct;

public final class WinBase {

    public static final int STD_OUTPUT_HANDLE = -11;

    public static class FILETIME implements Struct {

        public int dwLowDateTime;
        public int dwHighDateTime;

    }

}
