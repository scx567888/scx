package cool.scx.http.usagi.http2;

import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.usagi.ConnectionHandler;
import cool.scx.http.usagi.UsagiHttpServerOptions;
import cool.scx.tcp.ScxTCPSocket;

import java.io.IOException;
import java.util.function.Consumer;

//todo 未完成
public class Http2ConnectionHandler implements ConnectionHandler {

    @Override
    public String supportedApplicationProtocol() {
        return "h2";
    }

    @Override
    public void handle(ScxTCPSocket tcpSocket, UsagiHttpServerOptions options, Consumer<ScxHttpServerRequest> requestHandler) {
        //暂时不支持
        try {
            tcpSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
