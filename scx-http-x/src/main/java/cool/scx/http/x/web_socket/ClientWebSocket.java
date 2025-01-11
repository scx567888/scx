package cool.scx.http.x.web_socket;

import cool.scx.common.util.RandomUtils;
import cool.scx.http.web_socket.ScxClientWebSocket;
import cool.scx.http.web_socket.WebSocketOpCode;
import cool.scx.io.DataReader;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import static cool.scx.http.x.web_socket.WebSocketFrameHelper.writeFrame;

/**
 * ClientWebSocket
 *
 * @author scx567888
 * @version 0.0.1
 */
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
