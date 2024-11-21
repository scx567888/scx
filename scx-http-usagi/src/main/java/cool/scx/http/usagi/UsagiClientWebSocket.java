package cool.scx.http.usagi;

import cool.scx.http.ScxClientWebSocket;
import cool.scx.http.ScxWebSocket;
import cool.scx.http.WebSocketFrame;
import cool.scx.http.WebSocketOpCode;
import cool.scx.io.DataReader;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static cool.scx.http.WebSocketFrameHelper.readFrameUntilLast;
import static cool.scx.http.WebSocketFrameHelper.writeFrame;
import static cool.scx.http.WebSocketOpCode.*;

//todo 需要和 UsagiServerWebSocket 整合一下 抽象出一个 顶级类
public class UsagiClientWebSocket implements ScxClientWebSocket {

    private final DataReader reader;
    private final OutputStream writer;

    //为了防止底层的 OutputStream 被乱序写入 此处需要加锁
    private final ReentrantLock lock;

    private Consumer<String> textMessageHandler;
    private Consumer<byte[]> binaryMessageHandler;
    private Consumer<byte[]> pingHandler;
    private Consumer<byte[]> pongHandler;
    private BiConsumer<Integer, String> closeHandler;
    private Consumer<Throwable> errorHandler;
    private boolean isClosed;

    public UsagiClientWebSocket(DataReader reader, OutputStream writer) {
        this.reader = reader;
        this.writer = writer;
        this.lock = new ReentrantLock();
    }

    @Override
    public ScxWebSocket onTextMessage(Consumer<String> textMessageHandler) {
        this.textMessageHandler = textMessageHandler;
        return this;
    }

    @Override
    public ScxWebSocket onBinaryMessage(Consumer<byte[]> binaryMessageHandler) {
        this.binaryMessageHandler = binaryMessageHandler;
        return this;
    }

    @Override
    public ScxWebSocket onPing(Consumer<byte[]> pingHandler) {
        this.pingHandler = pingHandler;
        return this;
    }

    @Override
    public ScxWebSocket onPong(Consumer<byte[]> pongHandler) {
        this.pongHandler = pongHandler;
        return this;
    }

    @Override
    public ScxWebSocket onClose(BiConsumer<Integer, String> closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    @Override
    public ScxWebSocket onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ScxWebSocket send(String textMessage, boolean last) {
        byte[] payload = textMessage.getBytes();
        try {
            sendFrame(payload, TEXT, last);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ScxWebSocket send(byte[] binaryMessage, boolean last) {
        try {
            sendFrame(binaryMessage, BINARY, last);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public ScxWebSocket ping(byte[] data) {
        try {
            sendFrame(data, PING, true);
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
        }
        return this;
    }

    @Override
    public ScxWebSocket pong(byte[] data) {
        try {
            sendFrame(data, WebSocketOpCode.PONG, true);
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
        }
        return this;
    }

    @Override
    public ScxWebSocket close(int code, String reason) {
        if (isClosed) {
            return this;
        }
        isClosed = true;
        try {
            sendFrame(reason.getBytes(), WebSocketOpCode.CLOSE, true);
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
        }
        return this;
    }

    @Override
    public ScxWebSocket terminate() {
        isClosed = true;
        //todo 这里需要关闭 tcp 连接 ?
//        try {
//            scxTCPSocket.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return this;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    public void start() {
        while (true) {
            try {
                var frame = readFrameUntilLast(reader);
                handleFrame(frame);
            } catch (Exception e) {
                if (errorHandler != null) {
                    errorHandler.accept(e);
                }
                break; // 发生错误时退出循环
            }
        }
    }

    private void handleFrame(WebSocketFrame frame) {
        switch (frame.opCode()) {
            case CONTINUATION -> {
                // 理论上不会走到这里
            }
            case TEXT -> {
                textMessageHandler.accept(new String(frame.payloadData()));
            }
            case BINARY -> {
                binaryMessageHandler.accept(frame.payloadData());
            }
            case CLOSE -> {
                isClosed = true;
                closeHandler.accept(0, new String(frame.payloadData()));
            }
            case PING -> {
                pingHandler.accept(frame.payloadData());
            }
            case PONG -> {
                pongHandler.accept(frame.payloadData());
            }
        }
    }

    private void sendFrame(byte[] payload, WebSocketOpCode opcode, boolean last) throws IOException {
        lock.lock();
        try {
            var f = WebSocketFrame.of(last, opcode, payload);
            writeFrame(f, writer);
        } finally {
            lock.unlock();
        }
    }

}
