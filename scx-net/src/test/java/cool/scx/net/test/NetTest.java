package cool.scx.net.test;

import cool.scx.common.util.$;
import cool.scx.net.ScxTCPClientImpl;
import cool.scx.net.ScxTCPClientOptions;
import cool.scx.net.ScxTCPServerImpl;
import cool.scx.net.ScxTCPServerOptions;
import cool.scx.net.tls.TLS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class NetTest {

    static TLS tls;

    static {
        tls = null;
    }

    public static void main(String[] args) {
        test1();
        for (int i = 0; i < 1000; i++) {
            Thread.ofVirtual().start(() -> {
                test2();
            });
        }

    }

    public static void test1() {
        var tcpServer = new ScxTCPServerImpl(new ScxTCPServerOptions().port(8899).tls(tls));
        var i = new AtomicInteger(0);
        tcpServer.onConnect(c -> {
            int n = i.getAndIncrement();
            var b = new BufferedReader(new InputStreamReader(c.inputStream()));
            while (true) {
                try {
                    var s = b.readLine();
                    System.out.println(n + " " + c.socket().getRemoteSocketAddress() + " : " + s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        tcpServer.start();
        System.out.println("已监听端口号 : " + tcpServer.port());
    }

    public static void test2() {
        var tcpClient = new ScxTCPClientImpl(new ScxTCPClientOptions().tls(tls));
        var tcpSocket = tcpClient.connect(new InetSocketAddress(8899));
        try {
            var outputStream = tcpSocket.outputStream();
            $.Timeout[] timeout = new $.Timeout[1];
            AtomicInteger i = new AtomicInteger(0);

            while (true) {
                outputStream.write((i.getAndIncrement() + "\r\n").getBytes());
                outputStream.flush();
                $.sleep(50);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
