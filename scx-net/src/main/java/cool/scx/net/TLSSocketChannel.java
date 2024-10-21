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
    private final ByteBuffer writeBuffer;
    private final ByteBuffer readBuffer;
    private final ByteBuffer appData;
    private final ByteBuffer peerAppData;

    public TLSSocketChannel(SocketChannel socketChannel, SSLContext sslContext) {
        this.socketChannel = socketChannel;
        this.sslEngine = sslContext.createSSLEngine();
        this.writeBuffer = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
        this.readBuffer = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
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
                    if (socketChannel.read(readBuffer) == -1) {
                        throw new IOException("Channel closed during handshake");
                    }
                    readBuffer.flip();
                    SSLEngineResult unwrapResult = sslEngine.unwrap(readBuffer, peerAppData);
                    readBuffer.compact();
                    handshakeStatus = unwrapResult.getHandshakeStatus();
                    break;
                case NEED_WRAP:
                    writeBuffer.clear();
                    SSLEngineResult wrapResult = sslEngine.wrap(ByteBuffer.allocate(0), writeBuffer);
                    writeBuffer.flip();
                    while (writeBuffer.hasRemaining()) {
                        socketChannel.write(writeBuffer);
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
        int bytesRead = socketChannel.read(readBuffer);
        if (bytesRead == -1) {
            return -1;
        }
        readBuffer.flip();
        SSLEngineResult result = sslEngine.unwrap(readBuffer, dst);
        readBuffer.compact();
        if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
            socketChannel.close();
            return -1;
        }
        return dst.position();
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        //清空 写缓存
        writeBuffer.clear();
        var result = sslEngine.wrap(src, writeBuffer);
        var status = result.getStatus();
        switch (status) {
            case OK -> {
                //什么都不做
            }
            case BUFFER_OVERFLOW -> {
                throw new IOException("SSLEngine has BUFFER_OVERFLOW ");
            }
            case BUFFER_UNDERFLOW -> {
                throw new IOException("SSLEngine has BUFFER_UNDERFLOW ");
            }
            case CLOSED -> {
                // 关闭连接
                socketChannel.close();
                throw new IOException("SSLEngine has closed");
            }
        }
        //反转到读模式
        writeBuffer.flip();
        //写入到 socketChannel 中 这里我们一直调用直到完全写入 原因如下
        while (writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }
        // 我们这里不返回 socketChannel.write(writeBuffer) 的数据 (因为长度是加密后的)
        // 因为我们需要对外保持行为一致 返回的就是写入的明文的数据长度
        return result.bytesConsumed();
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
