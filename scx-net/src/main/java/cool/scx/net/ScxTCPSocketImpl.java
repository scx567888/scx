package cool.scx.net;

import cool.scx.io.NoMoreDataException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Path;

public class ScxTCPSocketImpl implements ScxTCPSocket {

    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public ScxTCPSocketImpl(Socket socket) {
        this.socket = socket;
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] read(int maxLength) throws IOException {
        var buffer = new byte[maxLength];
        int bytesRead = inputStream.read(buffer);
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
    public void write(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    @Override
    public void sendFile(Path path, long offset, long length) throws IOException {
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
                outputStream.write(buffer, 0, i);
                length -= i;
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
