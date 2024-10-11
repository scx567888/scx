package cool.scx.io.test.t;

import cool.scx.io.DataReader;

import java.io.InputStream;
import java.io.IOException;

public class DateReader1Impl implements DataReader {
    
    private final InputStream inputStream;
    private final byte[] buffer;
    private int head;
    private int tail;
    private int size;
    private int markPos;

    public DateReader1Impl(InputStream inputStream) {
        this.inputStream = inputStream;
        this.buffer = new byte[8192];
        this.head = 0;
        this.tail = 0;
        this.size = 0;
        this.markPos = -1;
    }

    @Override
    public byte read() {
        if (size == 0) {
            fillBuffer();
        }
        byte b = buffer[head];
        head = (head + 1) % buffer.length;
        size--;
        return b;
    }

    @Override
    public byte[] read(int length) {
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = read();
        }
        return result;
    }

    @Override
    public byte get() {
        if (size == 0) {
            fillBuffer();
        }
        return buffer[head];
    }

    @Override
    public byte[] get(int length) {
        byte[] result = new byte[length];
        int index = head;
        for (int i = 0; i < length; i++) {
            result[i] = buffer[index];
            index = (index + 1) % buffer.length;
        }
        return result;
    }

    @Override
    public int indexOf(byte b) {
        int index = head;
        for (int i = 0; i < size; i++) {
            if (buffer[index] == b) {
                return i;
            }
            index = (index + 1) % buffer.length;
        }
        return -1;
    }

    @Override
    public int indexOf(byte[] b) {
        for (int i = 0; i <= size - b.length; i++) {
            boolean found = true;
            for (int j = 0; j < b.length; j++) {
                if (buffer[(head + i + j) % buffer.length] != b[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void skip(int length) {
        for (int i = 0; i < length; i++) {
            read();
        }
    }

    private void fillBuffer() {
        try {
            int space = buffer.length - size;
            byte[] tempBuffer = new byte[space];
            int bytesRead = inputStream.read(tempBuffer);
            if (bytesRead != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    buffer[tail] = tempBuffer[i];
                    tail = (tail + 1) % buffer.length;
                    size++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading from input stream", e);
        }
    }
}
