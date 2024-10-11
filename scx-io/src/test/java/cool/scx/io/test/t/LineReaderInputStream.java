package cool.scx.io.test.t;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LineReaderInputStream extends InputStream {
    private final BufferedInputStream bis;
    private final byte[] buffer;
    private int bufferPos;
    private int bufferLimit;

    public LineReaderInputStream(InputStream inputStream) {
        this.bis = new BufferedInputStream(inputStream);
        this.buffer = new byte[8192];
        this.bufferPos = 0;
        this.bufferLimit = 0;
    }

    @Override
    public int read() throws IOException {
        if (bufferPos >= bufferLimit) {
            bufferLimit = bis.read(buffer);
            bufferPos = 0;
            if (bufferLimit == -1) {
                return -1;
            }
        }
        return buffer[bufferPos++] & 0xFF;
    }

    public String readLine() throws IOException {
        ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();
        while (true) {
            int b = read();
            if (b == -1 && lineBuffer.size() == 0) {
                return null; // EOF and no data read
            }
            if (b == -1 || b == '\n') {
                break; // Newline or EOF
            }
            if (b != '\r') {
                lineBuffer.write(b);
            }
        }
        return lineBuffer.toString();
    }

}
