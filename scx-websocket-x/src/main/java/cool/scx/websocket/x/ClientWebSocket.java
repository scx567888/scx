package cool.scx.websocket.x;

import cool.scx.common.util.RandomUtils;
import cool.scx.io.data_reader.DataReader;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.websocket.ScxClientWebSocket;
import cool.scx.websocket.WebSocketOpCode;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import static cool.scx.websocket.x.WebSocketFrameHelper.writeFrame;

/// ClientWebSocket
///
/// @author scx567888
/// @version 0.0.1
public class ClientWebSocket extends WebSocket implements ScxClientWebSocket {

    public ClientWebSocket(ScxTCPSocket tcpSocket, DataReader reader, OutputStream writer, WebSocketOptions options) {
        super(tcpSocket, reader, writer, options);
    }

    @Override
    public void sendFrame(WebSocketOpCode opcode, byte[] payload, boolean last) {
        lock.lock();
        try {
            // 和服务器端不同, 客户端的是需要发送掩码的
            var maskingKey = RandomUtils.randomBytes(4);
            var f = WebSocketFrame.of(last, opcode, maskingKey, payload);
            writeFrame(f, writer);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } finally {
            lock.unlock();
        }
    }

}
