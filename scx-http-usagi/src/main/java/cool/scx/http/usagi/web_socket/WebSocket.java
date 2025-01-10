package cool.scx.http.usagi.web_socket;

import cool.scx.http.usagi.web_socket.exception.WebSocketFrameTooBigException;
import cool.scx.http.usagi.web_socket.exception.WebSocketMessageTooBigException;
import cool.scx.http.web_socket.ScxWebSocket;
import cool.scx.http.web_socket.ScxWebSocketCloseInfoImpl;
import cool.scx.http.web_socket.WebSocketCloseInfo;
import cool.scx.http.web_socket.WebSocketOpCode;
import cool.scx.io.DataReader;
import cool.scx.io.NoMoreDataException;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;

import static cool.scx.http.usagi.web_socket.WebSocketFrameHelper.*;
import static cool.scx.http.web_socket.WebSocketCloseInfo.NORMAL_CLOSE;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class WebSocket extends AbstractWebSocket {

    protected final DataReader reader;
    protected final OutputStream writer;

    //为了防止底层的 OutputStream 被乱序写入 此处需要加锁
    protected final ReentrantLock lock;
    private final ScxTCPSocket tcpSocket;
    private final WebSocketOptions options;
    protected boolean isClosed;
    private boolean running;

    public WebSocket(ScxTCPSocket tcpSocket, DataReader reader, OutputStream writer, WebSocketOptions options) {
        this.tcpSocket = tcpSocket;
        this.reader = reader;
        this.writer = writer;
        this.options = options;
        this.lock = new ReentrantLock();
        this.running = true;
    }

    public void start() {
        while (running) {
            try {
                //尝试读取 帧
                var frame = options.mergeWebSocketFrame() ?
                        readFrameUntilLast(reader, options.maxWebSocketFrameSize(), options.maxWebSocketMessageSize()) :
                        readFrame(reader, options.maxWebSocketFrameSize());
                //处理帧
                handleFrame(frame);
            } catch (WebSocketFrameTooBigException | WebSocketMessageTooBigException e) {
                var c = new ScxWebSocketCloseInfoImpl(WebSocketCloseInfo.TOO_BIG.code(), "WebSocketFrameTooBig");
                doClose(c, e);
            } catch (WebSocketMessageTooBigException e) {
                var c = new ScxWebSocketCloseInfoImpl(WebSocketCloseInfo.TOO_BIG.code(), "WebSocketMessageTooBig");
                doClose(c, e);
            } catch (NoMoreDataException e) {
                var c = WebSocketCloseInfo.CLOSED_ABNORMALLY;
                //2, 关闭连接
                try {
                    //同样不需要处理错误
                    tcpSocket.close();
                } catch (IOException _) {

                }
                //3, 调用 close 处理器
                _callOnClose(e);
                //4, 停止监听
                stop();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }

    public void handleFrame(WebSocketFrame frame) {
        switch (frame.opCode()) {
            case CONTINUATION -> {
                // 理论上不会走到这里 todo
            }
            case TEXT -> _handleText(frame);
            case BINARY -> _handleBinary(frame);
            case PING -> _handlePing(frame);
            case PONG -> _handlePong(frame);
            case CLOSE -> _handleClose(frame);
        }
    }

    private void _handleText(WebSocketFrame frame) {
        _callOnTextMessage(new String(frame.payloadData()));
    }

    private void _handleBinary(WebSocketFrame frame) {
        _callOnBinaryMessage(frame.payloadData());
    }

    private void _handlePing(WebSocketFrame frame) {
        _callOnPing(frame.payloadData());
    }

    private void _handlePong(WebSocketFrame frame) {
        _callOnPong(frame.payloadData());
    }

    public void _handleClose(WebSocketFrame frame) {
        //1, 发送帧响应
        try {
            //这里有可能失败 但我们不需要调用做处理
            close(NORMAL_CLOSE);
        } catch (Exception _) {

        }
        //2, 关闭连接
        try {
            //同样不需要处理错误
            tcpSocket.close();
        } catch (IOException _) {

        }
        //3, 调用 close 处理器
        _callOnClose(parseCloseInfo(frame.payloadData()));
        //4, 停止监听
        stop();
    }

    private void doClose(ScxWebSocketCloseInfoImpl c, Exception e) {
        //1, 发送帧响应
        try {
            //这里有可能失败 但我们不需要调用做处理
            close(c);
        } catch (Exception _) {

        }
        //2, 关闭连接
        try {
            //同样不需要处理错误
            tcpSocket.close();
        } catch (IOException _) {

        }
        //3, 调用 close 处理器
        _callOnError(e);
        //4, 停止监听
        stop();
    }

    @Override
    public ScxWebSocket terminate() {
        isClosed = true;
        //todo 这里需要关闭 tcp 连接 ?
        try {
            tcpSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public void sendFrame(WebSocketOpCode opcode, byte[] payload, boolean last) throws IOException {
        lock.lock();
        try {
            var f = WebSocketFrame.of(last, opcode, payload);
            writeFrame(f, writer);
        } finally {
            lock.unlock();
        }
    }

}
