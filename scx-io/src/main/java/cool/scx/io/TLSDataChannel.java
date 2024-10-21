package cool.scx.io;

import cool.scx.io.DataChannel;
import cool.scx.io.NoMoreDataException;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;

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
        var session = sslEngine.getSession();
        this.myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        this.peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());
    }

    public void doHandshake() throws IOException {
        sslEngine.beginHandshake();
        var handshakeStatus = sslEngine.getHandshakeStatus();

        while (handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED &&
               handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {

            switch (handshakeStatus) {
                case NEED_UNWRAP:
                    if (channel.read(peerNetData) == -1) {
                        throw new IOException("Channel closed during handshake");
                    }
                    peerNetData.flip();
                    SSLEngineResult unwrapResult = sslEngine.unwrap(peerNetData, peerAppData);
                    peerNetData.compact();
                    handshakeStatus = unwrapResult.getHandshakeStatus();
                    break;
                case NEED_WRAP:
                    myNetData.clear();
                    SSLEngineResult wrapResult = sslEngine.wrap(ByteBuffer.allocate(0), myNetData);
                    myNetData.flip();
                    while (myNetData.hasRemaining()) {
                        channel.write(myNetData);
                    }
                    handshakeStatus = wrapResult.getHandshakeStatus();
                    break;
                case NEED_TASK:
                    Runnable task;
                    while ((task = sslEngine.getDelegatedTask()) != null) {
                        task.run();
                    }
                    handshakeStatus = sslEngine.getHandshakeStatus();
                    break;
                default:
                    throw new IllegalStateException("Unexpected SSL handshake status: " + handshakeStatus);
            }
        }
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            myAppData.clear();
            int limit = Math.min(buffer.remaining(), myAppData.capacity());
            for (int i = 0; i < limit; i++) {
                myAppData.put(buffer.get());
            }
            myAppData.flip();
            while (myAppData.hasRemaining()) {
                SSLEngineResult result = sslEngine.wrap(myAppData, myNetData);
                myNetData.flip();
                while (myNetData.hasRemaining()) {
                    channel.write(myNetData);
                }
                myNetData.compact();
            }
        }
    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        channel.read(peerNetData);
        peerNetData.flip();
        while (peerNetData.hasRemaining()) {
            try {
                SSLEngineResult result = sslEngine.unwrap(peerNetData, buffer);
                peerNetData.compact();
            } catch (SSLException e) {
                throw new RuntimeException(e);
            }
        }
        return buffer.position();
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
        // Not implemented yet
    }

    @Override
    public void write(Path path) throws IOException {
        // Not implemented yet
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
        // Not implemented yet
    }

    @Override
    public void read(Path path, OpenOption... options) throws IOException {
        // Not implemented yet
    }

    @Override
    public byte[] read(int maxLength) throws IOException, NoMoreDataException {
        ByteBuffer buffer = ByteBuffer.allocate(maxLength);
        int bytesRead = read(buffer);
        if (bytesRead == 0) {
            throw new NoMoreDataException();
        }
        byte[] bytes = new byte[bytesRead];
        buffer.flip();
        buffer.get(bytes);
        return bytes;
    }
    
}
