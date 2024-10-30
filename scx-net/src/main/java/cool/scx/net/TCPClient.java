package cool.scx.net;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.SocketAddress;

import static java.lang.System.Logger.Level.ERROR;

public class TCPClient implements ScxTCPClient {

    private static final System.Logger logger = System.getLogger(TCPClient.class.getName());

    private final ScxTCPClientOptions options;

    public TCPClient() {
        this(new ScxTCPClientOptions());
    }

    public TCPClient(ScxTCPClientOptions options) {
        this.options = options;
    }

    @Override
    public ScxTCPSocket connect(SocketAddress endpoint) {

        var tls = options.tls();

        //todo 处理代理
        var proxy = options.proxy();

        Socket socket;
        try {

            if (tls != null && tls.enabled()) {
                socket = tls.createSocket();
            } else {
                socket = new Socket();
            }

            socket.connect(endpoint);

            //主动调用握手 快速检测 ssl 错误 防止等到调用用户处理程序时才发现 
            if (socket instanceof SSLSocket sslSocket) {
                try {
                    sslSocket.startHandshake();
                } catch (IOException e) {
                    logger.log(ERROR, "SSL 握手失败 : " + e.getMessage());
                    try {
                        sslSocket.close();
                    } catch (IOException ce) {
                        e.addSuppressed(ce);
                    }
                    throw e;
                }
            }

        } catch (IOException e) {
            logger.log(ERROR, "连接失败 : ", e);
            throw new UncheckedIOException(e);
        }

        return new TCPSocket(socket);

    }

}
