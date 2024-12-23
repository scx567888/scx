package cool.scx.tcp;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.ReentrantLock;

//todo 未完成 有 bug
public class NioTLSTCPSocket implements ScxTCPSocket {

    private final SocketChannel socketChannel;
    private final SSLEngine sslEngine;
    private final ReentrantLock sslLock = new ReentrantLock();
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private ByteBuffer peerAppData;
    private ByteBuffer peerNetData;
    private ByteBuffer myAppData;
    private ByteBuffer myNetData;

    public NioTLSTCPSocket(SocketChannel socketChannel, SSLEngine sslEngine) {
        this.socketChannel = socketChannel;
        this.sslEngine = sslEngine;

        SSLSession session = sslEngine.getSession();
        this.peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        this.myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        this.myNetData = ByteBuffer.allocate(session.getPacketBufferSize());

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
        sslLock.lock();
        try {
            sslEngine.beginHandshake();

            while (true) {
                var handshakeStatus = sslEngine.getHandshakeStatus();
                switch (handshakeStatus) {
                    case NEED_UNWRAP -> {
                        if (socketChannel.read(peerNetData) < 0) {
                            throw new IOException("Failed to read data for unwrap.");
                        }
                        peerNetData.flip();
                        while (peerNetData.hasRemaining()) {
                            var unwrapResult = sslEngine.unwrap(peerNetData, peerAppData);
                            switch (unwrapResult.getStatus()) {
                                case OK -> {
                                }
                                case BUFFER_OVERFLOW -> peerAppData = enlargeApplicationBuffer(peerAppData);
                                case BUFFER_UNDERFLOW -> peerNetData = handleBufferUnderflow(peerNetData);
                                case CLOSED -> throw new SSLException("SSL Engine closed during handshake.");
                                default ->
                                        throw new IllegalStateException("Unexpected unwrap result: " + unwrapResult.getStatus());
                            }
                        }
                        peerNetData.compact();
                    }
                    case NEED_WRAP -> {
                        myNetData.clear();
                        var result = sslEngine.wrap(peerAppData, myNetData);
                        switch (result.getStatus()) {
                            case OK -> {
                                myNetData.flip();
                                while (myNetData.hasRemaining()) {
                                    socketChannel.write(myNetData);
                                }
                            }
                            case BUFFER_OVERFLOW -> throw new IOException("Buffer overflow during handshake wrap.");
                            case BUFFER_UNDERFLOW -> throw new SSLException("Buffer underflow during handshake wrap.");
                            case CLOSED -> throw new SSLException("SSL Engine closed during handshake.");
                            default -> throw new IllegalStateException("Unexpected wrap result: " + result.getStatus());
                        }
                    }
                    case NEED_TASK -> {
                        //todo 这里绝对正确
                        Runnable task;
                        while ((task = sslEngine.getDelegatedTask()) != null) {
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
        } finally {
            sslLock.unlock();
        }
    }

    private void runDelegatedTasks() {
        Runnable task;
        while ((task = sslEngine.getDelegatedTask()) != null) {
            task.run();
        }
    }
    
    

    public int read(ByteBuffer dst) throws IOException {
        sslLock.lock();
        try {
            int bytesRead = 0;
            // 确保在开始读取之前清空网络数据缓冲区
//            peerNetData.clear();

            while (true) {
                if (peerNetData.hasRemaining()) {
                    var result = sslEngine.unwrap(peerNetData, peerAppData);

                    switch (result.getStatus()) {
                        case OK -> {
                            peerAppData.flip();
                            while (peerAppData.hasRemaining() && dst.hasRemaining()) {
                                dst.put(peerAppData.get());
                                bytesRead++;
                            }
                            peerAppData.compact();
                            if (bytesRead > 0) {
                                return bytesRead;
                            }
                        }
                        case BUFFER_OVERFLOW -> peerAppData = enlargeApplicationBuffer(peerAppData);
                        case BUFFER_UNDERFLOW -> {
                            peerNetData.compact();
                            if (socketChannel.read(peerNetData) < 0) {
                                if (bytesRead == 0) {
                                    return -1;
                                }
                                break;
                            }
                            peerNetData.flip();
                        }
                        case CLOSED -> {
                            return -1;
                        }
                        default -> throw new SSLException("解密错误: " + result.getStatus());
                    }
                } else {
                    peerNetData.clear();
                    if (socketChannel.read(peerNetData) < 0) {
                        if (bytesRead == 0) {
                            return -1;
                        }
                        break;
                    }
                    peerNetData.flip();
                }
            }
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
                myNetData.clear();  // 重置位置和限制
                var result = sslEngine.wrap(buffer, myNetData);

                var status = result.getStatus();
                switch (status) {
                    case OK -> {
                        myNetData.flip();
                        while (myNetData.hasRemaining()) {
                            socketChannel.write(myNetData);
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
