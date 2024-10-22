package cool.scx.net.tls;

import cool.scx.io.ByteChannelDataSupplier;
import cool.scx.io.LinkedDataReader;
import cool.scx.io.ScxIOHelper;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import static cool.scx.net.tls.TLSCodec.enlargeBuffer;
import static cool.scx.net.tls.TLSCodec.ensureCapacity;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;

public final class TLSSocketChannel implements ByteChannel {

    private final SocketChannel socketChannel;
    private final SSLEngine sslEngine;
    private final ByteBuffer writeBuffer;
    private final ByteBuffer readBuffer;
    private final ByteBuffer appData;
    private final ByteBuffer peerAppData;
    private final LinkedDataReader dataReader;
    private final TLSCodec tlsCodec;
    private final LinkedDataReader rawReader;

    public TLSSocketChannel(SocketChannel socketChannel, SSLContext sslContext) {
        this.socketChannel = socketChannel;
        this.sslEngine = sslContext.createSSLEngine();
        this.tlsCodec = new TLSCodec(sslEngine);
        //写入缓冲 默认取 PacketBuffer 大小 (存储的永远是加密的数据)
        int packetBufferSize = sslEngine.getSession().getPacketBufferSize();
        int applicationBufferSize = sslEngine.getSession().getApplicationBufferSize();
        this.writeBuffer = ByteBuffer.allocate(packetBufferSize);
        this.readBuffer = ByteBuffer.allocate(packetBufferSize);
        this.appData = ByteBuffer.allocate(applicationBufferSize);
        this.peerAppData = ByteBuffer.allocate(applicationBufferSize);
        // 存储未解密的数据
        this.rawReader = new LinkedDataReader(new ByteChannelDataSupplier(socketChannel, applicationBufferSize));
        // 存储解密的数据
        this.dataReader = new LinkedDataReader(() -> {
            try {
                // 获取未解密的数据
                var encryptedBuffer = ByteBuffer.wrap(rawReader.fastPeek(packetBufferSize));

                //存储解密后的数据
                var decryptedData = ByteBuffer.allocate(applicationBufferSize);

                //循环解密
                while (encryptedBuffer.hasRemaining()) {
                    //临时解密数据
                    var tempDecryptedData = ByteBuffer.allocate(applicationBufferSize);
                    //解密
                    var result = sslEngine.unwrap(encryptedBuffer, tempDecryptedData);

                    //判断是否需要扩容等
                    switch (result.getStatus()) {
                        case OK -> {
                            tempDecryptedData.flip();
                            decryptedData = ensureCapacity(decryptedData, tempDecryptedData.remaining());
                            decryptedData.put(tempDecryptedData);
                            //移动 未解密数据的指针
                            int i = result.bytesConsumed();
                            rawReader.skip(i);
                        }
                        case BUFFER_OVERFLOW -> {
                            // 扩展应用数据缓冲区的大小
                            decryptedData = enlargeBuffer(decryptedData);
                        }
                        case BUFFER_UNDERFLOW -> {
                            // 缓冲区数据不足 需要扩容 我们以 2 倍为基准
                            encryptedBuffer = ByteBuffer.wrap(rawReader.fastPeek(packetBufferSize * 2));
                        }
                        case CLOSED -> {
                            // todo
                        }
                    }
                }

                //切换到读模式
                decryptedData.flip();

                return ScxIOHelper.toByteArray(decryptedData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
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
    public int read(ByteBuffer dst) {
        byte[] read2 = dataReader.fastRead(dst.remaining());
        dst.put(read2);
        return read2.length;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        var r = src.remaining();
        tlsCodec.encode(src, encode -> {
            while (encode.hasRemaining()) {
                socketChannel.write(encode);
            }
        });
        //tlsCodec 会保证将 src 的内容全部写入 所以 直接返回 r
        return r;
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

    public SocketAddress getRemoteAddress() throws IOException {
        return socketChannel.getRemoteAddress();
    }

}
