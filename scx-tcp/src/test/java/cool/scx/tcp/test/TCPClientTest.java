package cool.scx.tcp.test;

import cool.scx.tcp.ScxTCPClientOptions;
import cool.scx.tcp.TCPClient;

import java.io.IOException;
import java.net.InetSocketAddress;

import static cool.scx.tcp.test.TCPServerTest.tls;

public class TCPClientTest {

    public static void main(String[] args) throws InterruptedException, IOException {
        test1();
    }

    public static void test1() {
        //先启动服务器
        TCPServerTest.test1();

        // todo 优化性能 以及再虚拟线程中的 bug
        for (int j = 0; j < 10; j = j + 1) {
            Thread.ofVirtual().start(() -> {
                var tcpClient = new TCPClient(new ScxTCPClientOptions().tls(tls));
                var tcpSocket = tcpClient.connect(new InetSocketAddress(8899));
                try (tcpSocket) {
                    var out = tcpSocket.outputStream();
                    var i = 0;
                    while (i < 10000) {
                        out.write(((i = i + 1) + "\r\n" + (i = i + 1) + "\r\n").getBytes());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

}