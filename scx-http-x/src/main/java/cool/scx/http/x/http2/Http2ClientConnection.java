package cool.scx.http.x.http2;

import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.x.XHttpClientOptions;
import cool.scx.http.x.XHttpClientRequest;
import cool.scx.tcp.ScxTCPSocket;

public class Http2ClientConnection {

    public Http2ClientConnection(ScxTCPSocket tcpSocket, XHttpClientOptions options) {

    }

    public Http2ClientConnection sendRequest(XHttpClientRequest request, MediaWriter writer) {
        return null;
    }

    public ScxHttpClientResponse waitResponse() {
        return null;
    }

}
