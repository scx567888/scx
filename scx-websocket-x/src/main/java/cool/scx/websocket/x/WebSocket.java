package cool.scx.websocket.x;

import cool.scx.common.util.RandomUtils;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.exception.NoMoreDataException;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.websocket.ScxWebSocket;
import cool.scx.websocket.WebSocketFrame;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.locks.ReentrantLock;

import static cool.scx.websocket.WebSocketCloseInfo.NORMAL_CLOSE;
import static cool.scx.websocket.WebSocketOpCode.CLOSE;
import static cool.scx.websocket.x.WebSocketProtocolFrameHelper.writeFrame;

/// WebSocket
///
/// @author scx567888
/// @version 0.0.1
public class WebSocket implements ScxWebSocket {

    private final ScxTCPSocket tcpSocket;
    private final DataReader reader;
    private final OutputStream writer;
    private final WebSocketOptions options;
    //为了防止底层的 OutputStream 被乱序写入 此处需要加锁
    private final ReentrantLock lock;
    //是否是客户端 客户端需要加掩码
    private final boolean isClient;
    //限制只发送一次 close 帧
    protected boolean closeSent;

    public WebSocket(ScxTCPSocket tcpSocket, DataReader reader, OutputStream writer, WebSocketOptions options, boolean isClient) {
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
            var frame = options.mergeWebSocketFrame() ?
                    WebSocketProtocolFrameHelper.readFrameUntilLast(reader, options.maxWebSocketFrameSize(), options.maxWebSocketMessageSize()) :
                    WebSocketProtocolFrameHelper.readFrame(reader, options.maxWebSocketFrameSize());
            //当我们接收到了 close 帧 我们应该发送 close 帧并关闭
            if (frame.opCode() == CLOSE) {
                //1, 发送关闭响应帧
                try {
                    close(NORMAL_CLOSE); // 这里有可能无法发送 我们忽略异常
                } catch (Exception _) {

                }
                //2, 关闭底层 tcp 连接
                this.terminate();
            }
            return WebSocketFrame.of(frame.opCode(), frame.payloadData(), frame.fin());
        } catch (NoMoreDataException e) {
            throw new CloseWebSocketException(NORMAL_CLOSE.code(), NORMAL_CLOSE.reason());
        }
    }

    @Override
    public ScxWebSocket sendFrame(WebSocketFrame frame) {
        lock.lock();
        try {
            WebSocketProtocolFrame protocolFrame;
            // 和服务器端不同, 客户端的是需要发送掩码的
            if (isClient) {
                var maskingKey = RandomUtils.randomBytes(4);
                protocolFrame = WebSocketProtocolFrame.of(frame.fin(), frame.opCode(), maskingKey, frame.payloadData());
            } else {
                protocolFrame = WebSocketProtocolFrame.of(frame.fin(), frame.opCode(), frame.payloadData());
            }

            if (frame.opCode() == CLOSE) {
                //close 帧只允许成功发送一次
                if (closeSent) {
                    return this;
                }
                writeFrame(protocolFrame, writer);
                closeSent = true;
            } else {
                writeFrame(protocolFrame, writer);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            lock.unlock();
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

}
