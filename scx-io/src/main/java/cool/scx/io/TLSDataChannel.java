package cool.scx.io;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.OpenOption;
import java.nio.file.Path;

//todo 待完成
public class TLSDataChannel implements DataChannel {

    private final DataChannel channel;
    private final SSLEngine sslEngine;

    public TLSDataChannel(DataChannel channel, SSLEngine sslEngine) {
        this.channel = channel;
        this.sslEngine = sslEngine;
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {

    }

    @Override
    public void write(byte[] bytes, int offset, int length) throws IOException {

    }

    @Override
    public void write(byte[] bytes) throws IOException {

    }

    @Override
    public void write(Path path, long offset, long length) throws IOException {

    }

    @Override
    public void write(Path path) throws IOException {

    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] bytes, int offset, int length) throws IOException {
        return 0;
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return 0;
    }

    @Override
    public void read(Path path, long offset, long length, OpenOption... options) throws IOException {

    }

    @Override
    public void read(Path path, OpenOption... options) throws IOException {

    }

    @Override
    public byte[] read(int maxLength) throws IOException, NoMoreDataException {
        return new byte[0];
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean isOpen() throws IOException {
        return false;
    }

}
