package cool.scx.net;

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
    private final ByteBuffer myNetData;
    private final ByteBuffer peerNetData;
    private final ByteBuffer appData;
    private final ByteBuffer peerAppData;

    public TLSSocketChannel(SSLEngine sslEngine) throws IOException {
        this.socketChannel = SocketChannel.open();
        this.sslEngine = sslEngine;
        this.myNetData = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
        this.peerNetData = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
        this.appData = ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize());
        this.peerAppData = ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize());
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
                    myNetData.clear();
                    SSLEngineResult wrapResult = sslEngine.wrap(ByteBuffer.allocate(0), myNetData);
                    myNetData.flip();
                    while (myNetData.hasRemaining()) {
                        socketChannel.write(myNetData);
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

    public int write(ByteBuffer src) throws IOException {
        while (src.hasRemaining()) {
            myNetData.clear();
            SSLEngineResult result = sslEngine.wrap(src, myNetData);
            myNetData.flip();
            while (myNetData.hasRemaining()) {
                socketChannel.write(myNetData);
            }
            if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
                socketChannel.close();
                throw new IOException("SSLEngine has closed");
            }
        }
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    public void close() throws IOException {
        sslEngine.closeOutbound();
        startHandshake();
        socketChannel.close();
    }

}
