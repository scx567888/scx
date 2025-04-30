package cool.scx.http.x.http2;

import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.x.HttpClientOptions;
import cool.scx.http.x.HttpClientRequest;
import cool.scx.tcp.ScxTCPSocket;

public class Http2ClientConnection {

    public Http2ClientConnection(ScxTCPSocket tcpSocket, HttpClientOptions options) {

    }

    public Http2ClientConnection sendRequest(HttpClientRequest request, MediaWriter writer) {
        return null;
    }

    public ScxHttpClientResponse waitResponse() {
        return null;
    }

}
