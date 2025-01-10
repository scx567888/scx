package cool.scx.http.helidon;

import cool.scx.http.web_socket.ScxWebSocket;
import cool.scx.http.web_socket.WebSocketCloseInfo;
import cool.scx.http.web_socket.handler.BinaryMessageHandler;
import cool.scx.http.web_socket.handler.CloseHandler;
import cool.scx.http.web_socket.handler.TextMessageHandler;
import io.helidon.common.buffers.BufferData;
import io.helidon.http.Headers;
import io.helidon.http.HttpPrologue;
import io.helidon.websocket.WsListener;
import io.helidon.websocket.WsSession;
import io.helidon.websocket.WsUpgradeException;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * HelidonWebSocket
 *
 * @author scx567888
 * @version 0.0.1
 */
class HelidonWebSocket implements ScxWebSocket, WsListener {

    //todo 这种方式不准确
    private final AtomicBoolean closed = new AtomicBoolean(false);

    //必须加 因为 helidon 底层没有锁 如果在多个线程 send 会导致数据乱
    private final Lock lock;

    protected HttpPrologue prologue;
    protected Headers headers;
    protected WsSession wsSession;
    private TextMessageHandler textMessageHandler;
    private BinaryMessageHandler binaryMessageHandler;
    private Consumer<byte[]> pingHandler;
    private Consumer<byte[]> pongHandler;
    private CloseHandler closeHandler;
    private Consumer<Throwable> errorHandler;

    public HelidonWebSocket() {
        lock = new ReentrantLock();
    }

    @Override
    public HelidonWebSocket onTextMessage(TextMessageHandler textMessageHandler) {
        this.textMessageHandler = textMessageHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onBinaryMessage(BinaryMessageHandler binaryMessageHandler) {
        this.binaryMessageHandler = binaryMessageHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onPing(Consumer<byte[]> pingHandler) {
        this.pingHandler = pingHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onPong(Consumer<byte[]> pongHandler) {
        this.pongHandler = pongHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onClose(CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    @Override
    public HelidonWebSocket onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public ScxWebSocket send(String textMessage, boolean var2) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        lock.lock();
        try {
            wsSession.send(textMessage, var2);
        } finally {
            lock.unlock();
        }
        return this;
    }

    @Override
    public ScxWebSocket send(byte[] binaryMessage, boolean var2) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        lock.lock();
        try {
            wsSession.send(BufferData.create(binaryMessage), var2);
        } finally {
            lock.unlock();
        }
        return this;
    }


    @Override
    public ScxWebSocket ping(byte[] data) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        lock.lock();
        try {
            wsSession.ping(BufferData.create(data));
        } finally {
            lock.unlock();
        }
        return this;
    }

    @Override
    public ScxWebSocket pong(byte[] data) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        lock.lock();
        try {
            wsSession.pong(BufferData.create(data));
        } finally {
            lock.unlock();
        }
        return this;
    }

    @Override
    public ScxWebSocket close(int code, String reason) {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        lock.lock();
        try {
            wsSession.close(code, reason);
        } finally {
            lock.unlock();
            //此处主动调用 onClose
            onClose(null, WebSocketCloseInfo.NORMAL_CLOSE.code(), "主动关闭");
        }
        return this;
    }

    @Override
    public ScxWebSocket terminate() {
        if (wsSession == null) {
            throw new IllegalStateException("wsSession is null");
        }
        lock.lock();
        try {
            wsSession.terminate();
        } finally {
            lock.unlock();
        }
        return this;
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }

    @Override
    public void onMessage(WsSession session, String text, boolean last) {
        if (textMessageHandler != null) {
            textMessageHandler.handle(text, last);
        }
    }

    @Override
    public void onMessage(WsSession session, BufferData buffer, boolean last) {
        if (binaryMessageHandler != null) {
            binaryMessageHandler.handle(buffer.readBytes(), last);
        }
    }

    @Override
    public void onPing(WsSession session, BufferData buffer) {
        if (pingHandler != null) {
            pingHandler.accept(buffer.readBytes());
        }
    }

    @Override
    public void onPong(WsSession session, BufferData buffer) {
        if (pongHandler != null) {
            pongHandler.accept(buffer.readBytes());
        }
    }

    @Override
    public void onClose(WsSession session, int status, String reason) {
        this.closed.set(true);
        if (closeHandler != null) {
            closeHandler.handle(status, reason);
        }
    }

    @Override
    public void onError(WsSession session, Throwable t) {
        this.closed.set(true);
        if (errorHandler != null) {
            errorHandler.accept(t);
        }
    }

    @Override
    public void onOpen(WsSession session) {
        this.wsSession = session;
    }

    /**
     * onHttpUpgrade 一定是先于 onOpen 执行的 所以这里 赋值没有问题
     */
    @Override
    public Optional<Headers> onHttpUpgrade(HttpPrologue prologue, Headers headers) throws WsUpgradeException {
        this.prologue = prologue;
        this.headers = headers;
        return WsListener.super.onHttpUpgrade(prologue, headers);
    }

}
