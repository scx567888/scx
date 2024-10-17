package cool.scx.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.READ;

public class NioScxTCPSocketImpl implements ScxTCPSocket {

    private final SocketChannel socketChannel;

    public NioScxTCPSocketImpl(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public byte[] read(int maxLength) throws IOException {
        var buffer = ByteBuffer.allocate(maxLength);
        int bytesRead = this.socketChannel.read(buffer);
        if (bytesRead == maxLength) {
            // 如果读取的数据量与缓冲区大小一致，直接返回内部数组
            return buffer.array();
        } else {
            // 创建一个长度为实际读取字节数的字节数组
            var data = new byte[bytesRead];
            buffer.get(data);
            return data;
        }
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        var buffer = ByteBuffer.wrap(bytes);
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    @Override
    public void sendFile(Path path, long offset, long length) throws IOException {
        try (var fileChannel = FileChannel.open(path, READ)) {
            while (length > 0) {
                // transferTo 不保证一次既可以全部传输完毕 所以我们需要循环调用 
                long transferred = fileChannel.transferTo(offset, length, socketChannel);
                offset += transferred;
                length -= transferred;
            }
        }
    }

}
