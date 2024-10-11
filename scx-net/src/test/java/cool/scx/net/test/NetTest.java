package cool.scx.net.test;

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

import static java.lang.Thread.sleep;

public class NetTest {

    static TLS tls;

    static {
        tls = null;
    }

    public static void main(String[] args) throws InterruptedException {
        test1();
        test2();
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
                    if (s == null) {
                        c.socket().close();
                        tcpServer.stop();
                        return;
                    }
                    System.out.println(n + " " + c.socket().getRemoteSocketAddress() + " : " + s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        tcpServer.start();
        System.out.println("已监听端口号 : " + tcpServer.port());
    }

    public static void test2() throws InterruptedException {
        var tcpClient = new ScxTCPClientImpl(new ScxTCPClientOptions().tls(tls));
        var tcpSocket = tcpClient.connect(new InetSocketAddress(8899));
        try {
            var outputStream = tcpSocket.outputStream();

            AtomicInteger i = new AtomicInteger(0);

            while (i.get() < 100) {
                outputStream.write((i.getAndIncrement() + "\r\n").getBytes());
                outputStream.flush();
                sleep(50);
            }
            tcpSocket.socket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
