package cool.scx.http.x.web_socket;

import cool.scx.http.web_socket.WebSocketOpCode;
import cool.scx.io.DataReader;
import cool.scx.io.NoMoreDataException;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.locks.ReentrantLock;

import static cool.scx.http.web_socket.WebSocketCloseInfo.*;
import static cool.scx.http.x.web_socket.WebSocketFrameHelper.parseCloseInfo;
import static cool.scx.http.x.web_socket.WebSocketFrameHelper.writeFrame;
import static java.lang.System.Logger.Level.ERROR;

/**
 * WebSocket
 *
 * @author scx567888
 * @version 0.0.1
 */
public class WebSocket extends AbstractWebSocket {

    private static final System.Logger LOGGER = System.getLogger(WebSocket.class.getName());

    protected final ScxTCPSocket tcpSocket;
    protected final DataReader reader;
    protected final OutputStream writer;
    protected final WebSocketOptions options;
    //为了防止底层的 OutputStream 被乱序写入 此处需要加锁
    protected final ReentrantLock lock;
    protected ContinuationType continuationType;
    //限制只发送一次 close 帧
    protected boolean closeSent;
    private boolean running;

    public WebSocket(ScxTCPSocket tcpSocket, DataReader reader, OutputStream writer, WebSocketOptions options) {
        this.tcpSocket = tcpSocket;
        this.reader = reader;
        this.writer = writer;
        this.options = options;
        this.lock = new ReentrantLock();
        this.continuationType = ContinuationType.NONE;
        this.closeSent = false;
        this.running = false;
    }

    @Override
    public void startListening() {
        if (running) {
            return;
        }
        running = true;
        while (running) {
            try {
                //尝试读取 帧
                var frame = readFrame();
                //处理帧
                handleFrame(frame);
            } catch (CloseWebSocketException e) {
                _handleClose(NORMAL_CLOSE.code(), NORMAL_CLOSE.reason(), e.closeCode(), e.getMessage());
            } catch (Exception e) {
                _handleError(e);
                _handleClose(CLOSED_ABNORMALLY.code(), CLOSED_ABNORMALLY.reason(), UNEXPECTED_CONDITION.code(), e.getMessage());
            }
        }
    }

    public void stop() {
        this.running = false;
    }

    public WebSocketFrame readFrame() {
        try {
            return options.mergeWebSocketFrame() ?
                    WebSocketFrameHelper.readFrameUntilLast(reader, options.maxWebSocketFrameSize(), options.maxWebSocketMessageSize()) :
                    WebSocketFrameHelper.readFrame(reader, options.maxWebSocketFrameSize());
        } catch (NoMoreDataException e) {
            throw new CloseWebSocketException(NORMAL_CLOSE.code(), NORMAL_CLOSE.reason());
        }
    }

    private void handleFrame(WebSocketFrame frame) {
        switch (frame.opCode()) {
            case CONTINUATION -> _handleContinuation(frame);
            case TEXT -> _handleText(frame);
            case BINARY -> _handleBinary(frame);
            case PING -> _handlePing(frame);
            case PONG -> _handlePong(frame);
            case CLOSE -> _handleClose(frame);
        }
    }

    private void _handleContinuation(WebSocketFrame frame) {
        boolean finalFrame = frame.fin();
        var ct = continuationType;
        if (finalFrame) {
            continuationType = ContinuationType.NONE;
        }
        switch (ct) {
            case TEXT -> {
                try {
                    _callOnTextMessage(new String(frame.payloadData()), finalFrame);
                } catch (Exception e) {
                    LOGGER.log(ERROR, "Error while calling onTextMessage", e);
                }
            }
            case BINARY -> {
                try {
                    _callOnBinaryMessage(frame.payloadData(), finalFrame);
                } catch (Exception e) {
                    LOGGER.log(ERROR, "Error while calling onBinaryMessage", e);
                }
            }
            default -> {
                throw new CloseWebSocketException(PROTOCOL_ERROR.code(), "Unexpected continuation received");
            }
        }
    }

    private void _handleText(WebSocketFrame frame) {
        continuationType = ContinuationType.TEXT;
        try {
            _callOnTextMessage(new String(frame.payloadData()), frame.fin());
        } catch (Exception e) {
            LOGGER.log(ERROR, "Error while calling onTextMessage : ", e);
        }
    }

    private void _handleBinary(WebSocketFrame frame) {
        continuationType = ContinuationType.BINARY;
        try {
            _callOnBinaryMessage(frame.payloadData(), frame.fin());
        } catch (Exception e) {
            LOGGER.log(ERROR, "Error while call onBinaryMessage : ", e);
        }
    }

    private void _handlePing(WebSocketFrame frame) {
        try {
            _callOnPing(frame.payloadData());
        } catch (Exception e) {
            LOGGER.log(ERROR, "Error while call onPing : ", e);
        }
    }

    private void _handlePong(WebSocketFrame frame) {
        try {
            _callOnPong(frame.payloadData());
        } catch (Exception e) {
            LOGGER.log(ERROR, "Error while call onPong : ", e);
        }
    }

    private void _handleClose(WebSocketFrame frame) {
        var closeInfo = parseCloseInfo(frame.payloadData());
        _handleClose(closeInfo.code(), closeInfo.reason(), NORMAL_CLOSE.code(), NORMAL_CLOSE.reason());
    }

    public void _handleClose(int code, String reason, int peerCode, String peerReason) {
        //1, 调用用户处理器
        try {
            _callOnClose(code, reason);
        } catch (Exception e) {
            LOGGER.log(ERROR, "Error while call onClose : ", e);
        }
        //2, 发送关闭响应帧
        try {
            close(peerCode, peerReason); // 这里有可能无法发送 我们忽略异常
        } catch (Exception _) {

        }
        //3, 关闭 socket
        try {
            tcpSocket.close(); // 这里有可能已经被远端关闭 我们忽略异常
        } catch (IOException _) {

        }
        //4, 停止监听
        stop();
    }

    private void _handleError(Exception e) {
        try {
            _callOnError(e);
        } catch (Exception ex) {
            LOGGER.log(ERROR, "Error while call onError : ", ex);
        }
    }

    @Override
    public WebSocket close(int code, String reason) {
        //close 帧只允许成功发送一次
        if (closeSent) {
            return this;
        }
        super.close(code, reason);
        closeSent = true;
        return this;
    }

    @Override
    protected void sendFrame(WebSocketOpCode opcode, byte[] payload, boolean last) {
        lock.lock();
        try {
            var f = WebSocketFrame.of(last, opcode, payload);
            writeFrame(f, writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public WebSocket terminate() {
        _handleClose(NORMAL_CLOSE.code(), NORMAL_CLOSE.reason(), NORMAL_CLOSE.code(), "Terminate");
        return this;
    }

    @Override
    public boolean isClosed() {
        return tcpSocket.isClosed();
    }

    public enum ContinuationType {
        NONE,
        TEXT,
        BINARY
    }

}
