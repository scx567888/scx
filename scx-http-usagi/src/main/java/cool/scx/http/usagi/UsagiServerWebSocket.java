package cool.scx.http.usagi;

import cool.scx.http.ScxHttpHeaders;
import cool.scx.http.uri.ScxURI;
import cool.scx.http.usagi.http1x.Http1xServerWebSocketHandshakeRequest;
import cool.scx.http.web_socket.ScxServerWebSocket;
import cool.scx.io.DataReader;

import java.io.OutputStream;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class UsagiServerWebSocket extends UsagiWebSocket implements ScxServerWebSocket {

    private final Http1xServerWebSocketHandshakeRequest handshakeRequest;

    public UsagiServerWebSocket(Http1xServerWebSocketHandshakeRequest handshakeRequest, DataReader reader, OutputStream writer) {
        super(handshakeRequest.tcpSocket, reader, writer);
        this.handshakeRequest = handshakeRequest;
    }

    @Override
    public ScxURI uri() {
        return this.handshakeRequest.uri();
    }

    @Override
    public ScxHttpHeaders headers() {
        return this.handshakeRequest.headers();
    }

}
