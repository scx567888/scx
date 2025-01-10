package cool.scx.http.usagi.web_socket;

import cool.scx.common.util.RandomUtils;
import cool.scx.http.web_socket.ScxClientWebSocket;
import cool.scx.http.web_socket.WebSocketOpCode;
import cool.scx.io.DataReader;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.usagi.web_socket.WebSocketFrameHelper.writeFrame;

/**
 * UsagiClientWebSocket
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiClientWebSocket extends UsagiWebSocket implements ScxClientWebSocket {

    public UsagiClientWebSocket(ScxTCPSocket connect, DataReader reader, OutputStream writer, UsagiWebSocketOptions webSocketOptions) {
        super(connect, reader, writer, webSocketOptions);
    }

    @Override
    public void sendFrame(WebSocketOpCode opcode, byte[] payload, boolean last) throws IOException {
        lock.lock();
        try {
            // 和服务器端不同, 客户端的是需要发送掩码的
            var maskingKey = RandomUtils.randomBytes(4);
            var f = WebSocketFrame.of(last, opcode, maskingKey, payload);
            writeFrame(f, writer);
        } finally {
            lock.unlock();
        }
    }

}
