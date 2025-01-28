package cool.scx.tcp.tls;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class TLSAsynchronousSocketChannel extends AbstractAsynchronousSocketChannel {

    private final SSLEngine sslEngine;
    private ByteBuffer inboundNetData;
    private ByteBuffer outboundNetData;
    private ByteBuffer inboundAppData;

    public TLSAsynchronousSocketChannel(AsynchronousSocketChannel socketChannel, SSLEngine sslEngine) {
        super(socketChannel);
        this.sslEngine = sslEngine;
        var session = sslEngine.getSession();
        this.inboundNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        this.outboundNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        this.inboundAppData = ByteBuffer.allocate(session.getApplicationBufferSize()).flip();
    }

    @Override
    public <A> void read(ByteBuffer dst, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler) {
        inboundNetData.clear();
        socketChannel.read(inboundNetData, timeout, unit, attachment, new CompletionHandler<Integer, A>() {
            @Override
            public void completed(Integer result, A attachment) {
                if (result == -1) {
                    handler.completed(-1, attachment);
                    return;
                }

                inboundNetData.flip();
                try {
                    while (inboundNetData.hasRemaining()) {
                        sslEngine.unwrap(inboundNetData, dst);
                    }
                    handler.completed(result, attachment);
                } catch (SSLException e) {
                    handler.failed(e, attachment);
                }
            }

            @Override
            public void failed(Throwable exc, A attachment) {
                handler.failed(exc, attachment);
            }
        });
    }

    @Override
    public Future<Integer> read(ByteBuffer dst) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        read(dst, 0, TimeUnit.MILLISECONDS, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                future.complete(result);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                future.completeExceptionally(exc);
            }
        });
        return future;
    }

    @Override
    public <A> void read(ByteBuffer[] dsts, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler) {
        inboundNetData.clear();
        socketChannel.read(inboundNetData, timeout, unit, attachment, new CompletionHandler<Integer, A>() {
            @Override
            public void completed(Integer result, A attachment) {
                if (result == -1) {
                    handler.completed(-1L, attachment);
                    return;
                }

                inboundNetData.flip();
                try {
                    long totalBytes = 0;
                    while (inboundNetData.hasRemaining()) {
                        for (int i = offset; i < offset + length; i++) {
                            sslEngine.unwrap(inboundNetData, dsts[i]);
                            totalBytes += dsts[i].position();
                        }
                    }
                    handler.completed(totalBytes, attachment);
                } catch (SSLException e) {
                    handler.failed(e, attachment);
                }
            }

            @Override
            public void failed(Throwable exc, A attachment) {
                handler.failed(exc, attachment);
            }
        });
    }

    @Override
    public <A> void write(ByteBuffer src, long timeout, TimeUnit unit, A attachment, CompletionHandler<Integer, ? super A> handler) {
        outboundNetData.clear();
        try {
            sslEngine.wrap(src, outboundNetData);
            outboundNetData.flip();
        } catch (SSLException e) {
            handler.failed(e, attachment);
            return;
        }

        socketChannel.write(outboundNetData, timeout, unit, attachment, new CompletionHandler<Integer, A>() {
            @Override
            public void completed(Integer result, A attachment) {
                handler.completed(result, attachment);
            }

            @Override
            public void failed(Throwable exc, A attachment) {
                handler.failed(exc, attachment);
            }
        });
    }

    @Override
    public Future<Integer> write(ByteBuffer src) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        write(src, 0, TimeUnit.MILLISECONDS, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                future.complete(result);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                future.completeExceptionally(exc);
            }
        });
        return future;
    }

    @Override
    public <A> void write(ByteBuffer[] srcs, int offset, int length, long timeout, TimeUnit unit, A attachment, CompletionHandler<Long, ? super A> handler) {
        outboundNetData.clear();
        try {
            for (int i = offset; i < offset + length; i++) {
                sslEngine.wrap(srcs[i], outboundNetData);
            }
            outboundNetData.flip();
        } catch (SSLException e) {
            handler.failed(e, attachment);
            return;
        }

        socketChannel.write(outboundNetData, timeout, unit, attachment, new CompletionHandler<Integer, A>() {
            @Override
            public void completed(Integer result, A attachment) {
                handler.completed(result.longValue(), attachment);
            }

            @Override
            public void failed(Throwable exc, A attachment) {
                handler.failed(exc, attachment);
            }
        });
    }

    @Override
    public void close() throws IOException {
        sslEngine.closeOutbound();
        try {
            while (!sslEngine.isOutboundDone()) {
                ByteBuffer buffer = ByteBuffer.allocate(0);
                sslEngine.wrap(buffer, buffer);
            }
        } catch (SSLException e) {
            throw new IOException("Error closing TLS channel", e);
        }
        socketChannel.close();
    }

    public void startHandshake() throws SSLException {
        sslEngine.beginHandshake();
        while (true) {
            switch (sslEngine.getHandshakeStatus()) {
                case FINISHED, NOT_HANDSHAKING -> {
                    return;
                }
                case NEED_WRAP -> {
                    ByteBuffer buffer = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
                    sslEngine.wrap(ByteBuffer.allocate(0), buffer);
                    buffer.flip();
                    try {
                        socketChannel.write(buffer).get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new SSLException("Handshake wrap error", e);
                    }
                }
                case NEED_UNWRAP -> {
                    ByteBuffer buffer = ByteBuffer.allocate(sslEngine.getSession().getPacketBufferSize());
                    try {
                        socketChannel.read(buffer).get();
                        buffer.flip();
                        sslEngine.unwrap(buffer, ByteBuffer.allocate(sslEngine.getSession().getApplicationBufferSize()));
                    } catch (InterruptedException | ExecutionException e) {
                        throw new SSLException("Handshake unwrap error", e);
                    }
                }
                case NEED_TASK -> {
                    Runnable task;
                    while ((task = sslEngine.getDelegatedTask()) != null) {
                        task.run();
                    }
                }
                default -> throw new SSLException("Invalid SSL handshake status");
            }
        }
    }

    public SSLEngine sslEngine() {
        return sslEngine;
    }
}
