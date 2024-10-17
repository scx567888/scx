package cool.scx.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;

public class NioScxTCPSocketImpl implements ScxTCPSocket {

    private final SocketChannel socketChannel;

    public NioScxTCPSocketImpl(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public Socket socket() {
        return null;
    }

    @Override
    public byte[] read(int maxLength) throws IOException {
        var allocate = ByteBuffer.allocate(maxLength);
        int read = this.socketChannel.read(allocate);
        return allocate.array();
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        this.socketChannel.write(ByteBuffer.wrap(bytes));
    }

    @Override
    public void sendFile(Path path, long offset, long length) throws IOException {
        try (var fileChannel = FileChannel.open(path)) {
            while (length > 0) {
                // transferTo 不保证一次既可以全部传输完毕 所以我们需要循环调用 
                long transferred = fileChannel.transferTo(offset, length, socketChannel);
                offset += transferred;
                length -= transferred;
            }
        }
    }

}
