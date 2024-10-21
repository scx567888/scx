package cool.scx.io;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.OpenOption;
import java.nio.file.Path;

//todo 待完成
public class TLSDataChannel implements DataChannel {

    private final DataChannel channel;
    private final SSLEngine sslEngine;
    private final ByteBuffer myAppData;
    private final ByteBuffer myNetData;
    private final ByteBuffer peerAppData;
    private final ByteBuffer peerNetData;

    public TLSDataChannel(DataChannel channel, SSLEngine sslEngine) {
        this.channel = channel;
        this.sslEngine = sslEngine;
        SSLSession session = sslEngine.getSession();
        this.myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        this.peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        myAppData.put(buffer);
        myAppData.flip();
        while (myAppData.hasRemaining()) {
            SSLEngineResult result = sslEngine.wrap(myAppData, myNetData);
            myAppData.compact();
            myNetData.flip();
            channel.write(myNetData);
            myNetData.compact();
        }
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
        channel.read(peerNetData);
        peerNetData.flip();
        while (peerNetData.hasRemaining()) {
            SSLEngineResult result = sslEngine.unwrap(peerNetData, buffer);
            peerNetData.compact();
        }
        return buffer.position();
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
        sslEngine.closeOutbound();
        try {
            sslEngine.closeInbound();
        } catch (SSLException ignored) {
        }
        channel.close();
    }

    @Override
    public boolean isOpen() throws IOException {
        return channel.isOpen();
    }

}
