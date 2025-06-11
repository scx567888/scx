package cool.scx.websocket.x;

import cool.scx.bytes.ByteReader;
import cool.scx.bytes.exception.NoMoreDataException;
import cool.scx.common.util.RandomUtils;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.websocket.ScxWebSocket;
import cool.scx.websocket.WebSocketFrame;
import cool.scx.websocket.WebSocketHelper;
import cool.scx.websocket.exception.WebSocketException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.locks.ReentrantLock;

import static cool.scx.websocket.WebSocketOpCode.CLOSE;
import static cool.scx.websocket.close_info.WebSocketCloseInfo.NORMAL_CLOSE;
import static cool.scx.websocket.x.WebSocketProtocolFrameHelper.writeFrame;

/// WebSocket
///
/// @author scx567888
/// @version 0.0.1
public class WebSocket implements ScxWebSocket {

    private final ScxTCPSocket tcpSocket;
    private final ByteReader reader;
    private final OutputStream writer;
    private final WebSocketOptions options;
    //为了防止底层的 OutputStream 被乱序写入 此处需要加锁
    private final ReentrantLock lock;
    //是否是客户端 客户端需要加掩码
    private final boolean isClient;
    //限制只发送一次 close 帧
    protected boolean closeSent;

    public WebSocket(ScxTCPSocket tcpSocket, ByteReader reader, OutputStream writer, WebSocketOptions options, boolean isClient) {
        this.tcpSocket = tcpSocket;
        this.reader = reader;
        this.writer = writer;
        this.options = options;
        this.lock = new ReentrantLock();
        this.isClient = isClient;
    }

    @Override
    public WebSocketFrame readFrame() {
        try {
            var protocolFrame = readProtocolFrame();
            //当我们接收到了 close 帧 我们应该发送 close 帧并关闭
            if (protocolFrame.opCode() == CLOSE) {
                handleCloseFrame(protocolFrame);
            }
            return WebSocketFrame.of(protocolFrame.opCode(), protocolFrame.payloadData(), protocolFrame.fin());
        } catch (NoMoreDataException e) {
            throw new WebSocketException(NORMAL_CLOSE.code(), NORMAL_CLOSE.reason());
        }
    }

    @Override
    public ScxWebSocket sendFrame(WebSocketFrame frame) {
        if (isClosed()) {
            throw new IllegalStateException("Cannot send frame: WebSocket is already closed");
        }

        if (closeSent) {
            if (frame.opCode() == CLOSE) { //允许 用户多次发送 close 我们直接忽略
                return this;
            } else {// 其余则抛出异常
                throw new IllegalStateException("Cannot send non-close frames after a Close frame has been sent");
            }
        }

        var protocolFrame = createProtocolFrame(frame);

        try {
            writeProtocolFrame(protocolFrame);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        if (frame.opCode() == CLOSE) {
            closeSent = true;
        }

        return this;
    }

    @Override
    public ScxWebSocket terminate() {
        try {
            tcpSocket.close();  // 这里有可能已经被远端关闭 我们忽略异常
        } catch (IOException e) {

        }
        return this;
    }

    @Override
    public boolean isClosed() {
        return tcpSocket.isClosed();
    }

    private WebSocketProtocolFrame createProtocolFrame(WebSocketFrame frame) {
        // 和服务器端不同, 客户端的是需要发送掩码的
        if (isClient) {
            var maskingKey = RandomUtils.randomBytes(4);
            return WebSocketProtocolFrame.of(frame.fin(), frame.opCode(), maskingKey, frame.payloadData());
        } else {
            return WebSocketProtocolFrame.of(frame.fin(), frame.opCode(), frame.payloadData());
        }
    }

    private void writeProtocolFrame(WebSocketProtocolFrame protocolFrame) throws IOException {
        lock.lock();
        try {
            writeFrame(protocolFrame, writer);
        } finally {
            lock.unlock();
        }
    }

    private WebSocketProtocolFrame readProtocolFrame() {
        if (options.mergeWebSocketFrame()) {
            return WebSocketProtocolFrameHelper.readFrameUntilLast(reader, options.maxWebSocketFrameSize(), options.maxWebSocketMessageSize());
        } else {
            return WebSocketProtocolFrameHelper.readFrame(reader, options.maxWebSocketFrameSize());
        }
    }

    public void handleCloseFrame(WebSocketProtocolFrame protocolFrame) {
        var closeInfo = WebSocketHelper.parseCloseInfo(protocolFrame.payloadData());
        //1, 发送关闭响应帧
        try {
            close(closeInfo); // 这里有可能无法发送 我们忽略异常
        } catch (Exception _) {

        }
        //2, 关闭底层 tcp 连接
        this.terminate();
    }

}
