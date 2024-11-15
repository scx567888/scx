package cool.scx.http.usagi;

import cool.scx.http.ScxServerWebSocketHandshakeRequest;
import cool.scx.io.DataReader;

import java.io.OutputStream;

public class UsagiServerWebSocketHandshakeRequest extends UsagiHttpServerRequest implements ScxServerWebSocketHandshakeRequest {

    private final DataReader reader;
    private final OutputStream writer;
    public UsagiServerWebSocket webSocket;

    public UsagiServerWebSocketHandshakeRequest(DataReader reader, OutputStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public UsagiServerWebSocket webSocket() {
        if (webSocket == null) {
            acceptHandshake();
            webSocket = new UsagiServerWebSocket(this, reader, writer);
        }
        return webSocket;
    }

}
