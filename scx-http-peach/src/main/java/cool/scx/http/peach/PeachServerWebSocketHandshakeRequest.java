package cool.scx.http.peach;

import cool.scx.http.ScxServerWebSocketHandshakeRequest;
import cool.scx.io.DataReader;

import java.io.OutputStream;

public class PeachServerWebSocketHandshakeRequest extends PeachHttpServerRequest implements ScxServerWebSocketHandshakeRequest {

    private final DataReader reader;
    private final OutputStream writer;
    public PeachServerWebSocket webSocket;

    public PeachServerWebSocketHandshakeRequest(DataReader reader, OutputStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public PeachServerWebSocket webSocket() {
        if (webSocket == null) {
            acceptHandshake();
            webSocket = new PeachServerWebSocket(this, reader, writer);
        }
        return webSocket;
    }

}
