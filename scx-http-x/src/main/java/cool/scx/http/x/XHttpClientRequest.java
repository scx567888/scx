package cool.scx.http.x;

import cool.scx.http.ScxHttpClientRequestBase;
import cool.scx.http.ScxHttpClientResponse;
import cool.scx.http.media.MediaWriter;
import cool.scx.http.x.http1.Http1ClientConnection;
import cool.scx.http.x.http2.Http2ClientConnection;
import cool.scx.tcp.ScxTCPSocket;

/// todo 待完成
///
/// @author scx567888
/// @version 0.0.1
public class XHttpClientRequest extends ScxHttpClientRequestBase {

    private final XHttpClient httpClient;
    private final XHttpClientOptions options;
    public ScxTCPSocket tcpSocket;

    public XHttpClientRequest(XHttpClient httpClient) {
        this.httpClient = httpClient;
        this.options = httpClient.options();
    }

    @Override
    public ScxHttpClientResponse send(MediaWriter writer) {

        this.tcpSocket = httpClient.createTCPSocket(uri, getApplicationProtocols());

        var useHttp2 = false;

        if (this.tcpSocket.isTLS()) {
            var applicationProtocol = this.tcpSocket.tlsManager().getApplicationProtocol();
            useHttp2 = "h2".equals(applicationProtocol);
        }

        if (useHttp2) {
            return new Http2ClientConnection(tcpSocket, options).sendRequest(this, writer).waitResponse();
        } else {
            return new Http1ClientConnection(tcpSocket, options).sendRequest(this, writer).waitResponse();
        }

    }

    private String[] getApplicationProtocols() {
        if (this.options.enableHttp2()) {
            return new String[]{"http/1.1", "h2"};
        } else {
            return new String[]{"http/1.1"};
        }
    }

}
