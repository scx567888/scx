package cool.scx.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ScxInputStream extends BufferedInputStream {

    public ScxInputStream(InputStream in) {
        super(in);
    }

    public ScxInputStream(InputStream in, int size) {
        super(in, size);
    }

    public byte[] readMatch(byte[] match) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int matchIndex = 0;
        int b;

        while ((b = this.read()) != -1) {

            if (b == match[matchIndex]) {
                matchIndex++;
                if (matchIndex == match.length) {
                    // 完全匹配，返回之前的内容
                    return buffer.toByteArray();
                } else {
                    buffer.write(b);
                }
            } else {
                // 部分匹配失败，保存部分匹配的状态
                matchIndex = matchFailure(match, matchIndex, b);
                if (matchIndex == 0) {
                    buffer.write(b);
                }
            }
        }

        throw new IllegalArgumentException("match not found");
    }

    private static int matchFailure(byte[] match, int matchIndex, int b) {
        // 保存部分匹配状态
        for (int i = matchIndex - 1; i >= 0; i--) {
            if (match[i] == b) {
                return i + 1;
            }
        }
        return 0;
    }

}
