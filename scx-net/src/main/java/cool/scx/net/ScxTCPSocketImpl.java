package cool.scx.net;

import cool.scx.io.IOStreamDataChannel;
import cool.scx.io.NoMoreDataException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class ScxTCPSocketImpl extends IOStreamDataChannel implements ScxTCPSocket {

    private final Socket socket;

    public ScxTCPSocketImpl(Socket socket) {
        super(getInputStream(socket), getOutputStream(socket));
        this.socket = socket;
    }

    private static InputStream getInputStream(Socket socket) {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static OutputStream getOutputStream(Socket socket) {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(Path path, long offset, long length) throws IOException {
        try (var raf = new RandomAccessFile(path.toFile(), "r")) {
            //先移动文件指针
            raf.seek(offset);
            //循环发送
            var buffer = new byte[8192];
            while (length > 0) {
                int i = raf.read(buffer, 0, (int) Math.min(buffer.length, length));
                if (i == -1) {
                    break; // 处理文件结束情况
                }
                out.write(buffer, 0, i);
                length -= i;
            }
        }
    }

    @Override
    public void write(Path path) throws IOException {
        try (var is = Files.newInputStream(path)) {
            is.transferTo(out);
        }
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        byte[] bytes = new byte[buffer.remaining()];
        int bytesRead = read(bytes, 0, bytes.length);
        buffer.put(bytes, 0, bytesRead);
        return bytesRead;
    }

    @Override
    public byte[] read(int maxLength) throws IOException {
        var buffer = new byte[maxLength];
        int bytesRead = in.read(buffer);
        if (bytesRead == -1) {
            throw new NoMoreDataException();
        }
        if (bytesRead == maxLength) {
            // 如果读取的数据量与缓冲区大小一致，直接返回内部数组
            return buffer;
        } else {
            //返回指定长度 
            var data = new byte[bytesRead];
            System.arraycopy(buffer, 0, data, 0, data.length);
            return data;
        }
    }

    @Override
    public void read(Path path, long offset, long length, OpenOption... options) throws IOException {
        try (var outputFile = new RandomAccessFile(path.toFile(), "rw")) {
            outputFile.seek(offset);

            byte[] buffer = new byte[8192];
            while (length > 0) {
                int bytesRead = in.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                outputFile.write(buffer, 0, bytesRead);
                length -= bytesRead;
            }
        }
    }

    @Override
    public void read(Path path,OpenOption... options) throws IOException {
        try (var outputFile = new RandomAccessFile(path.toFile(), "rw")) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while (true) {
                bytesRead = in.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                outputFile.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return socket.getRemoteSocketAddress();
    }

}
