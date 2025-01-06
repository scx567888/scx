package cool.scx.tcp;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 经典 TCP 客户端
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ClassicTCPClient implements ScxTCPClient {

    private final ScxTCPClientOptions options;

    public ClassicTCPClient() {
        this(new ScxTCPClientOptions());
    }

    public ClassicTCPClient(ScxTCPClientOptions options) {
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
                socket = tls.socketFactory().createSocket();
            } else {
                socket = new Socket();
            }
            socket.connect(endpoint);
        } catch (IOException e) {
            throw new UncheckedIOException("客户端连接失败 !!!", e);
        }

        // 主动调用握手 快速检测 SSL 错误 防止等到调用用户处理程序时才发现 
        if (socket instanceof SSLSocket sslSocket) {
            try {
                sslSocket.startHandshake();
            } catch (IOException e) {
                try {
                    sslSocket.close();
                } catch (IOException ce) {
                    e.addSuppressed(ce);
                }
                throw new UncheckedIOException("客户端 SSL 握手失败 !!!", e);
            }
        }

        return new ClassicTCPSocket(socket);

    }

}
