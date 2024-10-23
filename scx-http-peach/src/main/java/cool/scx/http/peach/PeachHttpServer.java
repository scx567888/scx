package cool.scx.http.peach;

import cool.scx.http.ScxHttpServer;
import cool.scx.http.ScxHttpServerOptions;
import cool.scx.http.ScxHttpServerRequest;
import cool.scx.http.ScxServerWebSocket;
import cool.scx.io.ByteChannelDataSupplier;
import cool.scx.io.LinkedDataReader;
import cool.scx.net.ScxTCPServer;
import cool.scx.net.ScxTCPServerOptions;
import cool.scx.net.ScxTCPSocket;
import cool.scx.net.TCPServer;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class PeachHttpServer implements ScxHttpServer {

    private final Function<ScxTCPServerOptions, ScxTCPServer> tcpServerBuilder;
    private final ScxTCPServer tcpServer;
    private final ScxHttpServerOptions options;
    private Consumer<ScxHttpServerRequest> requestHandler;
    private Consumer<ScxServerWebSocket> webSocketHandler;
    private Consumer<Throwable> errorHandler;

    public PeachHttpServer(ScxHttpServerOptions options,Function<ScxTCPServerOptions, ScxTCPServer> tcpServerBuilder) {
        this.options=options;
        this.tcpServerBuilder = tcpServerBuilder;
        this.tcpServer = tcpServerBuilder.apply(new ScxTCPServerOptions().port(options.port()));
        this.tcpServer.onConnect(this::listen);
    }

    public PeachHttpServer(ScxHttpServerOptions options) {
       this(options, TCPServer::new);
    }

    public PeachHttpServer() {
        this(new ScxHttpServerOptions());
    }

    private void listen(ScxTCPSocket scxTCPSocket) {
        var dataReader=new LinkedDataReader(()-> {
            try {
                byte[] read = scxTCPSocket.read(8192);
                return new LinkedDataReader.Node(read);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        while (true){
            byte[] bytes = dataReader.readUntil("\r\n".getBytes());
            System.out.println(new String(bytes));
        }
    }

    @Override
    public ScxHttpServer requestHandler(Consumer<ScxHttpServerRequest> handler) {
        this.requestHandler=handler;
        return this;
    }

    @Override
    public ScxHttpServer webSocketHandler(Consumer<ScxServerWebSocket> handler) {
        this.webSocketHandler=handler;
        return this;
    }

    @Override
    public ScxHttpServer errorHandler(Consumer<Throwable> handler) {
        this.errorHandler=handler;
        return this;
    }

    @Override
    public void start() {
        tcpServer.start();
    }

    @Override
    public void stop() {
        tcpServer.stop();
    }

    @Override
    public int port() {
        return tcpServer.port();
    }

}
