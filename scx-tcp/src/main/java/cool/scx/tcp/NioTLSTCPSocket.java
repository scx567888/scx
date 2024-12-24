package cool.scx.tcp;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.ReentrantLock;

/**
 * app data
 * <p>
 * |           ^
 * |     |     |
 * v     |     |
 * +----+-----|-----+----+
 * |          |          |
 * |       SSL|Engine    |
 * wrap()  |          |          |  unwrap()
 * | OUTBOUND | INBOUND  |
 * |          |          |
 * +----+-----|-----+----+
 * |     |     ^
 * |     |     |
 * v           |
 * <p>
 * net data
 */
//todo 未完成 有 bug
public class NioTLSTCPSocket implements ScxTCPSocket {

    private final SocketChannel socketChannel;
    private final SSLEngine sslEngine;
    private final ReentrantLock sslLock = new ReentrantLock();
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final ByteBuffer outboundAppData;
    private final ByteBuffer outboundNetData;
    private final ByteBuffer inboundAppData;
    private final ByteBuffer inboundNetData;


    public NioTLSTCPSocket(SocketChannel socketChannel, SSLEngine sslEngine) {
        this.socketChannel = socketChannel;
        this.sslEngine = sslEngine;

        SSLSession session = sslEngine.getSession();
        this.outboundAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.outboundNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        this.inboundAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.inboundNetData = ByteBuffer.allocate(session.getPacketBufferSize());

        this.inputStream = createInputStream();
        this.outputStream = createOutputStream();
    }

    private ByteBuffer enlargeApplicationBuffer(ByteBuffer buffer) {
        SSLSession session = sslEngine.getSession();
        return ByteBuffer.allocate(session.getApplicationBufferSize() + buffer.capacity());
    }

    private ByteBuffer handleBufferUnderflow(ByteBuffer buffer) throws SSLException {
        if (sslEngine.getSession().getPacketBufferSize() > buffer.capacity()) {
            return enlargePacketBuffer(buffer);
        } else {
            buffer.compact();
            return buffer;
        }
    }

    private ByteBuffer enlargePacketBuffer(ByteBuffer buffer) {
        return ByteBuffer.allocate(buffer.capacity() * 2);
    }


    private InputStream createInputStream() {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                ByteBuffer buffer = ByteBuffer.allocate(1);
                return NioTLSTCPSocket.this.read(buffer) > 0 ? buffer.get() & 0xFF : -1;
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                ByteBuffer buffer = ByteBuffer.wrap(b, off, len);
                return NioTLSTCPSocket.this.read(buffer);
            }
        };
    }

    private OutputStream createOutputStream() {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                ByteBuffer buffer = ByteBuffer.allocate(1);
                buffer.put((byte) b);
                buffer.flip();
                NioTLSTCPSocket.this.write(buffer);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                System.err.println(new String(b, off, len));
                ByteBuffer buffer = ByteBuffer.wrap(b, off, len);
                NioTLSTCPSocket.this.write(buffer);
            }
        };
    }

    public void startHandshake() throws IOException {
        sslEngine.beginHandshake();

        while (true) {
            var handshakeStatus = sslEngine.getHandshakeStatus();
            switch (handshakeStatus) {
                case NEED_UNWRAP -> {
                    //读取远程数据到入站网络缓冲区
                    if (socketChannel.read(inboundNetData) == -1) {
                        throw new IOException("Channel closed during handshake");
                    }
                    //切换成读模式
                    inboundNetData.flip();
                    //尝试解密
                    while (inboundNetData.hasRemaining()) {
                        var result = sslEngine.unwrap(inboundNetData, inboundAppData);
                        switch (result.getStatus()) {
                            case OK -> {
                                // 解密成功，直接继续进行，因为握手阶段 即使 unwrap , inboundAppData 也只会是空 所以跳过处理
                            }
                            case BUFFER_OVERFLOW -> {
                                // inboundAppData 容量太小 无法容纳解密后的数据 需要扩容 inboundAppData
                                //todo 暂不处理
                                System.out.println("buffer overflow");
                            }
                            case BUFFER_UNDERFLOW -> {
                                // inboundNetData 数据不够解密 需要继续获取
                                //todo 暂不处理
                                System.out.println("buffer underflow");
                            }
                            case CLOSED -> {
                                //todo 暂不处理
                                System.out.println("closed");
                            }
                        }
                    }
                    inboundNetData.compact();
                }
                case NEED_WRAP -> {
                    //清空出站网络缓冲区
                    outboundNetData.clear();
                    //加密一个空的数据 这里因为待加密数据是空的 所以不需要循环去加密 单次执行即可
                    var result = sslEngine.wrap(ByteBuffer.allocate(0), outboundNetData);
                    switch (result.getStatus()) {
                        case OK -> {
                            //切换到读模式
                            outboundNetData.flip();
                            //循环发送到远端
                            while (outboundNetData.hasRemaining()) {
                                socketChannel.write(outboundNetData);
                            }
                        }
                        case BUFFER_OVERFLOW -> {
                            //todo 理论上不会到这里
                            System.out.println("buffer overflow");
                        }
                        case BUFFER_UNDERFLOW -> {
                            //todo 理论上不会到这里
                            System.out.println("buffer underflow");
                        }
                        case CLOSED -> {
                            //todo 理论上不会到这里
                            System.out.println("closed");
                        }
                    }
                }
                case NEED_TASK -> {
                    //todo 这里绝对正确
                    while (true) {
                        var task = sslEngine.getDelegatedTask();
                        if (task == null) {
                            break;
                        }
                        task.run();
                    }
                }
                case FINISHED -> {
                    //退出循环 todo 这里绝对正确
                    System.err.println("Handshake finished.");
                    return;
                }
                case NOT_HANDSHAKING -> {
                    //退出循环 todo 这里绝对正确
                    System.err.println("Handshake not handled.");
                    return;
                }
            }
        }
    }


    public int read(ByteBuffer dst) throws IOException {
        sslLock.lock();
        try {
            int bytesRead = 0;
            // 确保在开始读取之前清空网络数据缓冲区
//            peerNetData.clear();

//            while (true) {
//                if (peerNetData.hasRemaining()) {
//                    var result = sslEngine.unwrap(peerNetData, peerAppData);
//
//                    switch (result.getStatus()) {
//                        case OK -> {
//                            peerAppData.flip();
//                            while (peerAppData.hasRemaining() && dst.hasRemaining()) {
//                                dst.put(peerAppData.get());
//                                bytesRead++;
//                            }
//                            peerAppData.compact();
//                            if (bytesRead > 0) {
//                                return bytesRead;
//                            }
//                        }
//                        case BUFFER_OVERFLOW -> peerAppData = enlargeApplicationBuffer(peerAppData);
//                        case BUFFER_UNDERFLOW -> {
//                            peerNetData.compact();
//                            if (socketChannel.read(peerNetData) < 0) {
//                                if (bytesRead == 0) {
//                                    return -1;
//                                }
//                                break;
//                            }
//                            peerNetData.flip();
//                        }
//                        case CLOSED -> {
//                            return -1;
//                        }
//                        default -> throw new SSLException("解密错误: " + result.getStatus());
//                    }
//                } else {
//                    peerNetData.clear();
//                    if (socketChannel.read(peerNetData) < 0) {
//                        if (bytesRead == 0) {
//                            return -1;
//                        }
//                        break;
//                    }
//                    peerNetData.flip();
//                }
//            }
            return bytesRead;
        } finally {
            sslLock.unlock();
        }
    }


    //todo 这个方法已经完成
    public void write(ByteBuffer buffer) throws IOException {
        sslLock.lock();
        try {
            while (buffer.hasRemaining()) {
                //重置
                outboundNetData.clear();
                //加密
                var result = sslEngine.wrap(buffer, outboundNetData);
                switch (result.getStatus()) {
                    case OK -> {
                        //切换到读模式
                        outboundNetData.flip();
                        //循环发送
                        while (outboundNetData.hasRemaining()) {
                            socketChannel.write(outboundNetData);
                        }
                    }
                    // 不应该发生在 wrap 操作中      
                    case BUFFER_OVERFLOW -> throw new IOException("SSLEngine wrap 遇到 BUFFER_OVERFLOW");
                    // 不应该发生在 wrap 操作中  
                    case BUFFER_UNDERFLOW -> throw new IOException("SSLEngine wrap 遇到 BUFFER_UNDERFLOW");
                    // 不应该发生在 wrap 操作中
                    case CLOSED -> throw new IOException("SSLEngine wrap 遇到 CLOSED");
                }
            }
        } finally {
            sslLock.unlock();
        }
    }

    @Override
    public InputStream inputStream() {
        return inputStream;
    }

    @Override
    public OutputStream outputStream() {
        return outputStream;
    }

    @Override
    public boolean isClosed() {
        return !socketChannel.isOpen();
    }

    @Override
    public SocketAddress remoteAddress() throws IOException {
        return socketChannel.getRemoteAddress();
    }

    @Override
    public void close() throws IOException {
        sslLock.lock();
        try {
            sslEngine.closeOutbound();
            socketChannel.close();
        } finally {
            sslLock.unlock();
        }
    }

}
