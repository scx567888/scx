package cool.scx.net;

import cool.scx.io.NoMoreDataException;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

public class NioScxTCPSocketImpl implements ScxTCPSocket {

    private final SocketChannel socketChannel;

    public NioScxTCPSocketImpl(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    @Override
    public void write(byte[] bytes, int offset, int length) throws IOException {
        write(ByteBuffer.wrap(bytes, offset, length));
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        write(ByteBuffer.wrap(bytes));
    }

    @Override
    public void write(Path path, long offset, long length) throws IOException {
        try (var fileChannel = FileChannel.open(path, READ)) {
            while (length > 0) {
                // transferTo 不保证一次既可以全部传输完毕 所以我们需要循环调用 
                var i = fileChannel.transferTo(offset, length, socketChannel);
                offset += i;
                length -= i;
            }
        }
    }

    @Override
    public void write(Path path) throws IOException {
        try (var fileChannel = FileChannel.open(path, READ)) {
            long offset = 0;
            long length = fileChannel.size();
            while (length > 0) {
                // transferTo 不保证一次既可以全部传输完毕 所以我们需要循环调用 
                var i = fileChannel.transferTo(offset, length, socketChannel);
                offset += i;
                length -= i;
            }
        }
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException, NoMoreDataException {
        return 0;
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException, NoMoreDataException {
        var buffer = ByteBuffer.wrap(bytes, offset, length);
        return this.socketChannel.read(buffer);
    }

    @Override
    public int read(byte[] bytes) throws IOException, NoMoreDataException {
        var buffer = ByteBuffer.wrap(bytes);
        return this.socketChannel.read(buffer);
    }

    @Override
    public int read(Path path, long offset, long length) throws IOException {
        try (var fileChannel = FileChannel.open(path, WRITE)) {
            while (length > 0) {
                // transferTo 不保证一次既可以全部传输完毕 所以我们需要循环调用 
                var i = fileChannel.transferFrom(socketChannel, offset, length);
                offset += i;
                length -= i;
            }
        }
    }

    @Override
    public int read(Path path) throws IOException {
        return 0;
    }

    @Override
    public byte[] read(int maxLength) throws IOException {
        var buffer = ByteBuffer.allocate(maxLength);
        int bytesRead = this.socketChannel.read(buffer);
        if (bytesRead == -1) {
            throw new NoMoreDataException();
        }
        if (bytesRead == maxLength) {
            // 如果读取的数据量与缓冲区大小一致，直接返回内部数组
            return buffer.array();
        } else {
            //因为 ByteBuffer 每次都是重新创建的 (position 为 0) 所以我们直接 arraycopy 是可以的 
            var data = new byte[bytesRead];
            System.arraycopy(buffer.array(), 0, data, 0, data.length);
            return data;
        }
    }


    @Override
    public void receiveFile(Path path, long offset, long length) throws IOException {
        try (var fileChannel = FileChannel.open(path, WRITE)) {
            while (length > 0) {
                // transferTo 不保证一次既可以全部传输完毕 所以我们需要循环调用 
                var i = fileChannel.transferFrom(socketChannel, offset, length);
                offset += i;
                length -= i;
            }
        }
    }

    @Override
    public void sendFile(Path path, long offset, long length) throws IOException {

    }

    @Override
    public void close() throws IOException {
        socketChannel.close();
    }

    @Override
    public SocketAddress getRemoteAddress() throws IOException {
        return socketChannel.getRemoteAddress();
    }

}
