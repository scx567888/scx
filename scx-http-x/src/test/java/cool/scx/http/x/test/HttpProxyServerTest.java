package cool.scx.http.x.test;

import cool.scx.http.method.HttpMethod;
import cool.scx.http.x.HttpClient;
import cool.scx.http.x.HttpServer;
import cool.scx.http.x.http1.Http1ClientResponse;
import cool.scx.http.x.http1.Http1ServerRequest;
import cool.scx.tcp.ScxTCPSocket;
import cool.scx.tcp.TCPClient;

import java.io.IOException;
import java.net.InetSocketAddress;

// 测试代理功能
public class HttpProxyServerTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    /// 只做流量转发 不做任何解析
    public static void test1() throws IOException {
        var httpServer = new HttpServer();
        httpServer.onRequest(c -> {

            var request = (Http1ServerRequest) c;

            // https 隧道请求
            if (c.method() == HttpMethod.CONNECT) {
                System.out.println("收到 HTTPS 代理请求 : " + c.uri());
                //1, 获取连接对象
                var serverConnection = request.connection;
                //2, 停止循环
                serverConnection.stop();
                //3, 获取 当前连接的 底层 tcpSocket 内容
                var serverTCPSocket = serverConnection.tcpSocket;
                //4, 创建 远端连接
                var tcpClient = new TCPClient();
                ScxTCPSocket clientTCPSocket;
                try {
                    clientTCPSocket = tcpClient.connect(new InetSocketAddress(c.uri().host(), c.uri().port()));
                } catch (IOException e) {
                    throw new RuntimeException("连接远端失败 !!!", e);
                }

                //5, 通知代理连接成功
                request.response().reasonPhrase("连接成功!!!").status(200).send();

                //开启两个线程 进行数据相互传输 其中 tls 相关内容 我们直接原封不动传输
                Thread.ofVirtual().start(() -> {
                    try {
                        var outputStream = serverTCPSocket.outputStream();
                        var inputStream = clientTCPSocket.inputStream();
                        inputStream.transferTo(outputStream);
                    } catch (IOException e) {
                        try {
                            serverTCPSocket.close();
                            clientTCPSocket.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                Thread.ofVirtual().start(() -> {
                    try {
                        var outputStream = clientTCPSocket.outputStream();
                        var inputStream = serverTCPSocket.inputStream();
                        inputStream.transferTo(outputStream);
                    } catch (IOException e) {
                        try {
                            serverConnection.close();
                            clientTCPSocket.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

            } else {
                // 普通 Http 代理

                System.out.println("收到 HTTP 代理请求 : " + c.uri());

                var httpClient = new HttpClient();

                var response = (Http1ClientResponse) httpClient.request()
                        .method(request.method())
                        .uri(request.uri())
                        .headers(request.headers())
                        .send(request.body().inputStream());

                request.response()
                        .reasonPhrase(response.reasonPhrase())
                        .status(response.status())
                        .headers(response.headers())
                        .send(response.body().inputStream());

            }
        });

        httpServer.start(17890);
    }

    /// 尝试解码内容 可以拓展为抓包工具
    public static void test2() {
        // 这里 https 需要 伪造 tls 证书, 完成握手之后 后续步骤和 test1 相同 即可解密内容
    }

}
