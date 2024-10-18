package cool.scx.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.READ;

public class ByteChannelDataChannel<C extends ByteChannel> implements DataChannel {

    protected final C channel;

    public ByteChannelDataChannel(C channel) {
        this.channel = channel;
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            channel.write(buffer);
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
                var i = fileChannel.transferTo(offset, length, channel);
                if (i == 0) {
                    break; // No more data left to write
                }
                offset += i;
                length -= i;
            }
        }
    }

    @Override
    public void write(Path path) throws IOException {
        try (var fileChannel = FileChannel.open(path, READ)) {
            long offset = 0;
            //这样读取 size 速度最快
            while (true) {
                // transferTo 不保证一次既可以全部传输完毕 所以我们需要循环调用 
                var i = fileChannel.transferTo(offset, Long.MAX_VALUE, channel);
                if (i == 0) {
                    break; // No more data left to write
                }
                offset += i;
            }
        }
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        return this.channel.read(buffer);
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        return read(ByteBuffer.wrap(bytes, offset, length));
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return read(ByteBuffer.wrap(bytes));
    }

    @Override
    public void read(Path path, long offset, long length, OpenOption... options) throws IOException {
        try (var fileChannel = FileChannel.open(path, options)) {
            while (length > 0) {
                // transferTo 不保证一次既可以全部传输完毕 所以我们需要循环调用 
                var i = fileChannel.transferFrom(channel, offset, length);
                if (i == 0) {
                    break; // No more data left to read
                }
                offset += i;
                length -= i;
            }
        }
    }

    @Override
    public void read(Path path, OpenOption... options) throws IOException {
        try (var fileChannel = FileChannel.open(path, options)) {
            long offset = 0;
            while (true) {
                long i = fileChannel.transferFrom(channel, offset, Long.MAX_VALUE);
                if (i == 0) {
                    break; // No more data left to read
                }
                offset += i;
            }
        }
    }

    @Override
    public byte[] read(int maxLength) throws IOException, NoMoreDataException {
        var buffer = ByteBuffer.allocate(maxLength);
        int bytesRead = this.channel.read(buffer);
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
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public boolean isOpen() throws IOException {
        return channel.isOpen();
    }

}
