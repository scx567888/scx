package cool.scx.common.ffm.platform.win32;

public class Win32Exception extends RuntimeException {

    public Win32Exception(int rc) {
        super("Win32 Error: " + rc);
    }

}
