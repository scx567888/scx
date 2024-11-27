package cool.scx.http.usagi;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.http.web_socket.ScxServerWebSocketHandshakeRequest;
import cool.scx.io.DataReader;

import java.io.OutputStream;

public class UsagiServerWebSocket extends UsagiWebSocket implements ScxServerWebSocket {

    private final ScxServerWebSocketHandshakeRequest handshakeServerRequest;

    public UsagiServerWebSocket(ScxServerWebSocketHandshakeRequest handshakeServerRequest, DataReader reader, OutputStream writer) {
        super(reader, writer);
        this.handshakeServerRequest = handshakeServerRequest;
    }

    @Override
    public ScxURI uri() {
        return this.handshakeServerRequest.uri();
    }

    @Override
    public ScxHttpHeaders headers() {
        return this.handshakeServerRequest.headers();
    }

}
