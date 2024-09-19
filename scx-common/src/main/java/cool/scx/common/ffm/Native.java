package cool.scx.common.ffm;

public class Native {

    public static String toString(char[] buf) {
        int len = buf.length;

        for (int index = 0; index < len; ++index) {
            if (buf[index] == 0) {
                len = index;
                break;
            }
        }

        return len == 0 ? "" : new String(buf, 0, len);
    }

}
