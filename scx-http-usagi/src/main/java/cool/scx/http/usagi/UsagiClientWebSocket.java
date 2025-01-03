package cool.scx.http.usagi;

import cool.scx.common.util.RandomUtils;
import cool.scx.http.web_socket.ScxClientWebSocket;
import cool.scx.http.web_socket.WebSocketFrame;
import cool.scx.http.web_socket.WebSocketOpCode;
import cool.scx.io.DataReader;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.web_socket.WebSocketFrameHelper.writeFrame;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiClientWebSocket extends UsagiWebSocket implements ScxClientWebSocket {

    public UsagiClientWebSocket(ScxTCPSocket connect, DataReader reader, OutputStream writer) {
        super(connect, reader, writer);
    }

    @Override
    public void sendFrame(byte[] payload, WebSocketOpCode opcode, boolean last) throws IOException {
        lock.lock();
        try {
            var maskingKey = RandomUtils.randomBytes(4);
            var f = WebSocketFrame.of(last, opcode, maskingKey, payload);
            writeFrame(f, writer);
        } finally {
            lock.unlock();
        }
    }

}
