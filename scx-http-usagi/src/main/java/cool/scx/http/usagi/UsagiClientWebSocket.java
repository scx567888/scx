package cool.scx.http.usagi;

import cool.scx.common.util.RandomUtils;
import cool.scx.http.ScxClientWebSocket;
import cool.scx.http.WebSocketFrame;
import cool.scx.http.WebSocketOpCode;
import cool.scx.io.DataReader;

import java.io.IOException;
import java.io.OutputStream;

import static cool.scx.http.WebSocketFrameHelper.writeFrame;

public class UsagiClientWebSocket extends UsagiWebSocket implements ScxClientWebSocket {

    public UsagiClientWebSocket(DataReader reader, OutputStream writer) {
        super(reader, writer);
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
