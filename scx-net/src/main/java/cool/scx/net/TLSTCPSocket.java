package cool.scx.net;

import cool.scx.io.ByteChannelDataSupplier;
import cool.scx.io.LinkedDataReader;
import cool.scx.io.NoMoreDataException;

import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;

import static cool.scx.io.BufferHelper.expandBuffer;
import static cool.scx.io.BufferHelper.putBytes;
import static javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;

//todo 未完成 在高并发的时候 内存占用较高
public class TLSTCPSocket implements ScxTCPSocket {

    private final SocketChannel socketChannel;

    private final SSLEngine sslEngine;
    private final LinkedDataReader dataReader;
    private final LinkedDataReader rawReader;
    private final int packetBufferSize;
    private final int applicationBufferSize;
    // 避免每次都创建 减少内存占用
    private final ByteBuffer writeBuffer;

    public TLSTCPSocket(SocketChannel socketChannel, SSLEngine sslEngine) {
        this.socketChannel = socketChannel;
        this.sslEngine = sslEngine;
        //写入缓冲 默认取 PacketBuffer 大小 (存储的永远是加密的数据)
        this.packetBufferSize = sslEngine.getSession().getPacketBufferSize();
        this.applicationBufferSize = sslEngine.getSession().getApplicationBufferSize();
        // 存储未解密的数据
        this.rawReader = new LinkedDataReader(new ByteChannelDataSupplier(socketChannel, applicationBufferSize));
        // 存储解密的数据
        this.dataReader = new LinkedDataReader(this::decodeDataSupplier);
        this.writeBuffer = ByteBuffer.allocateDirect(packetBufferSize);
    }

    //todo 在虚拟线程中运行时 有 bug
    public void startHandshake() throws IOException {
        sslEngine.beginHandshake();
        var peerAppData = ByteBuffer.allocate(applicationBufferSize);
        var readBuffer = ByteBuffer.allocate(packetBufferSize);
        while (true) {

            var handshakeStatus = sslEngine.getHandshakeStatus();

            switch (handshakeStatus) {
                // 退出循环
                case FINISHED, NOT_HANDSHAKING -> {
                    return;
                }
                case NEED_UNWRAP -> {
                    if (socketChannel.read(readBuffer) == -1) {
                        throw new IOException("Channel closed during handshake");
                    }
                    readBuffer.flip();
                    //todo 在虚拟线程中 可能陷入无限循环 既 readBuffer.hasRemaining() 永为 true
                    while (readBuffer.hasRemaining()) {
                        var unwrapResult = sslEngine.unwrap(readBuffer, peerAppData);
                    }
                    readBuffer.compact();
                }
                case NEED_WRAP -> {
                    writeBuffer.clear();
                    var wrapResult = sslEngine.wrap(ByteBuffer.allocate(0), writeBuffer);
                    writeBuffer.flip();
                    while (writeBuffer.hasRemaining()) {
                        socketChannel.write(writeBuffer);
                    }
                }
                case NEED_TASK -> {
                    Runnable task;
                    while ((task = sslEngine.getDelegatedTask()) != null) {
                        task.run();
                    }
                }
                default -> throw new IllegalStateException("Unexpected handshake status: " + handshakeStatus);
            }
        }
    }

    public LinkedDataReader.Node decodeDataSupplier() {
        try {
            // 未解密的数据
            var encryptedBuffer = ByteBuffer.allocate(packetBufferSize);

            // 解密后的数据
            var decryptedBuffer = ByteBuffer.allocate(applicationBufferSize);

            //填充数据并切换到读模式
            encryptedBuffer = putBytes(encryptedBuffer, rawReader.fastRead(packetBufferSize)).flip();

            //循环解密
            while (encryptedBuffer.hasRemaining()) {

                var result = sslEngine.unwrap(encryptedBuffer, decryptedBuffer);

                switch (result.getStatus()) {
                    // 解密成功 什么都不需要做
                    case OK -> {
                    }
                    // decryptedBuffer 容量 不足 扩容
                    case BUFFER_OVERFLOW -> decryptedBuffer = expandBuffer(decryptedBuffer);
                    // 未解密数据不足 我们需要继续获取 
                    case BUFFER_UNDERFLOW ->
                            encryptedBuffer = putBytes(encryptedBuffer.compact(), rawReader.fastRead(packetBufferSize)).flip();
                    case CLOSED -> {
                    }
                }
            }

            //切换到读模式
            decryptedBuffer.flip();

            return new LinkedDataReader.Node(decryptedBuffer.array(), decryptedBuffer.position(), decryptedBuffer.limit());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            writeBuffer.clear();  // 重置位置和限制
            var result = sslEngine.wrap(buffer, writeBuffer);

            var status = result.getStatus();
            switch (status) {
                case OK -> {
                    writeBuffer.flip();
                    while (writeBuffer.hasRemaining()) {
                        socketChannel.write(writeBuffer);
                    }
                }
                case BUFFER_OVERFLOW -> throw new IOException("SSLEngine wrap 遇到 BUFFER_OVERFLOW");
                case BUFFER_UNDERFLOW -> throw new IOException("SSLEngine wrap 遇到 BUFFER_UNDERFLOW");
                case CLOSED -> throw new IOException("SSLEngine wrap 遇到 CLOSED");
            }
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

    }

    @Override
    public void write(Path path) throws IOException {

    }

    @Override
    public int read(ByteBuffer buffer) throws IOException {
        var bytes = dataReader.tryRead(buffer.remaining());
        buffer.put(bytes);
        return bytes.length;
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

    }

    @Override
    public void read(Path path, OpenOption... options) throws IOException {

    }

    @Override
    public byte[] read(int maxLength) throws IOException, NoMoreDataException {
        return dataReader.tryRead(maxLength);
    }


    @Override
    public void close() throws IOException {
        sslEngine.closeOutbound();
        if (sslEngine.getHandshakeStatus() != NOT_HANDSHAKING) {
            startHandshake();
        }
        socketChannel.close();
    }

    @Override
    public boolean isOpen() {
        return socketChannel.isOpen();
    }

    @Override
    public SocketAddress remoteAddress() throws IOException {
        return socketChannel.getRemoteAddress();
    }

}
