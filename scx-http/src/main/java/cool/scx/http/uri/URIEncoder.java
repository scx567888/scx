package cool.scx.http.uri;

import java.nio.CharBuffer;

import static java.nio.charset.StandardCharsets.UTF_8;

public class URIEncoder {

    private static final char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    //按照 codePoint 排列
    private static final boolean[] DONT_NEED_ENCODING_URI = initDontNeedEncoding(
            "!", "#", "$", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            ":", ";", "=", "?", "@",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "_",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "~"
    );

    private static final boolean[] DONT_NEED_ENCODING_URI_COMPONENT = initDontNeedEncoding(
            "!", "'", "(", ")", "*", "-", ".",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "_",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "~"
    );

    /**
     * 此方法会生成一个 128 位的 boolean 数组 索引表示 codePoint 值表示是否需要编码
     *
     * @param c a
     * @return a
     */
    private static boolean[] initDontNeedEncoding(String... c) {
        var table = new boolean[128];
        for (var s : c) {
            var i = s.codePointAt(0);
            table[i] = true;
        }
        return table;
    }

    private static String encode(String str, boolean[] dontNeedEncoding) {
        var sb = new StringBuilder();
        var codePoints = str.codePoints().toArray();
        for (int codePoint : codePoints) {
            //如果 不需要编码
            if (codePoint < 128 && dontNeedEncoding[codePoint]) {
                sb.appendCodePoint(codePoint);
            } else {
                appendUTF8EncodedCharacter(sb, codePoint);
            }
        }
        return sb.toString();
    }

    public static String encodeURI(String uri) {
        return encode(uri, DONT_NEED_ENCODING_URI);
    }

    public static String encodeURIComponent(String uriComponent) {
        return encode(uriComponent, DONT_NEED_ENCODING_URI_COMPONENT);
    }

    private static void appendUTF8EncodedCharacter(StringBuilder sb, int codePoint) {
        var chars = CharBuffer.wrap(Character.toChars(codePoint));
        var bytes = UTF_8.encode(chars);

        while (bytes.hasRemaining()) {
            appendEscape(sb, bytes.get() & 0xFF);
        }
    }

    private static void appendEscape(StringBuilder appender, int b) {
        appender.append('%');
        appender.append(HEX_DIGITS[b >> 4]);
        appender.append(HEX_DIGITS[b & 0x0F]);
    }

}
