package cool.scx.http.x.http1.chunked;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.x.http1.Http1Helper.CHUNKED_END_BYTES;
import static cool.scx.http.x.http1.Http1Helper.CRLF_BYTES;

public class HttpChunkedOutputStream extends OutputStream {

    private static final byte[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private final OutputStream out;

    public HttpChunkedOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        write(new byte[]{(byte) b}, 0, 1);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        // 0 长度无需写入
        if (len == 0) {
            return;
        }
        // 写入分块
        writeHexLength(len);    // 写入块大小
        out.write(CRLF_BYTES);  // 写入 块大小 结束符
        out.write(b, off, len); // 写入 数据块 内容
        out.write(CRLF_BYTES);  // 写入 分块 结束符
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        //写入终结分块
        out.write(CHUNKED_END_BYTES);
        out.close();
    }

    /// 直接写入十六进制表示的块大小
    private void writeHexLength(int value) throws IOException {
        // 最大值 0xFFFFFFFF（32 位无符号整数）转换为十六进制最多 8 个字符
        var bytes = new byte[8];
        var pos = 8;
        do {
            bytes[--pos] = HEX_DIGITS[value & 0xF]; // 取最后 4 位
            value = value >>> 4; // 右移 4 位
        } while (value != 0);

        out.write(bytes, pos, 8 - pos);
    }

    public OutputStream outputStream() {
        return out;
    }

}
