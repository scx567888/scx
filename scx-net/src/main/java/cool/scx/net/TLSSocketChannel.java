package cool.scx.net;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;

import static javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;

public final class TLSSocketChannel implements ByteChannel {

    private final SocketChannel socketChannel;
    private final SSLEngine sslEngine;
    private final ByteBuffer netData;
    private final ByteBuffer peerNetData;
    private final ByteBuffer appData;
    private final ByteBuffer peerAppData;

    public TLSSocketChannel(SocketChannel socketChannel, SSLContext sslContext) {
        this.socketChannel = socketChannel;
        this.sslEngine = sslContext.createSSLEngine();
        this.netData = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
        this.peerNetData = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
        this.appData = ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize());
        this.peerAppData = ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize());
    }

    public TLSSocketChannel(SSLContext sslContext) throws IOException {
        this(SocketChannel.open(), sslContext);
    }

    public void setUseClientMode(boolean useClientMode) {
        sslEngine.setUseClientMode(useClientMode);
    }

    public void startHandshake() throws IOException {
        sslEngine.beginHandshake();
        var handshakeStatus = sslEngine.getHandshakeStatus();

        while (handshakeStatus != FINISHED && handshakeStatus != NOT_HANDSHAKING) {

            switch (handshakeStatus) {
                case NEED_UNWRAP:
                    if (socketChannel.read(peerNetData) == -1) {
                        throw new IOException("Channel closed during handshake");
                    }
                    peerNetData.flip();
                    SSLEngineResult unwrapResult = sslEngine.unwrap(peerNetData, peerAppData);
                    peerNetData.compact();
                    handshakeStatus = unwrapResult.getHandshakeStatus();
                    break;
                case NEED_WRAP:
                    netData.clear();
                    SSLEngineResult wrapResult = sslEngine.wrap(ByteBuffer.allocate(0), netData);
                    netData.flip();
                    while (netData.hasRemaining()) {
                        socketChannel.write(netData);
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
                    throw new IllegalStateException("Unexpected handshake status: " + handshakeStatus);
            }
        }
    }

    public boolean connect(SocketAddress remote) throws IOException {
        return socketChannel.connect(remote);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        int bytesRead = socketChannel.read(peerNetData);
        if (bytesRead == -1) {
            return -1;
        }
        peerNetData.flip();
        SSLEngineResult result = sslEngine.unwrap(peerNetData, dst);
        peerNetData.compact();
        if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
            socketChannel.close();
            return -1;
        }
        return dst.position();
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        netData.clear();
        SSLEngineResult result = sslEngine.wrap(src, netData);
        netData.flip();
        int totalBytesWritten = socketChannel.write(netData);
        if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
            socketChannel.close();
            throw new IOException("SSLEngine has closed");
        }
        return totalBytesWritten;
    }

    @Override
    public boolean isOpen() {
        return socketChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        sslEngine.closeOutbound();
        if (sslEngine.getHandshakeStatus() != NOT_HANDSHAKING) {
            startHandshake();
        }
        socketChannel.close();
    }

}
