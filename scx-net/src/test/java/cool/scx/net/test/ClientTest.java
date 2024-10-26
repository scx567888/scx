package cool.scx.net.test;

import cool.scx.net.ScxTCPClientOptions;
import cool.scx.net.TCPClient;

import java.io.IOException;
import java.net.InetSocketAddress;

import static cool.scx.net.test.ServerTest.tls;

public class ClientTest {

    public static void main(String[] args) throws InterruptedException, IOException {
        test1();
    }

    public static void test1() throws InterruptedException, IOException {
        //先启动服务器
        ServerTest.test1();

        // todo 优化性能 以及再虚拟线程中的 bug
        for (int j = 0; j < 10; j++) {
            Thread.ofVirtual().start(() -> {
                var tcpClient = new TCPClient(new ScxTCPClientOptions().tls(tls));
                var tcpSocket = tcpClient.connect(new InetSocketAddress(8899));
                var out=tcpSocket.outputStream();
                try {
                    var i = 0;
                    while (i < 10000) {
                        out.write((i++ + "\r\n" + i++ + "\r\n").getBytes());
//                $.sleep(50);
                    }
//            tcpSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }

}
