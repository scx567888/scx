package cool.scx.net.test;

import cool.scx.io.LinkedDataReader;
import cool.scx.net.*;
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

    public static void main(String[] args) throws InterruptedException, IOException {
        test1();
        test2();
    }

    public static void test1() {
        var tcpServer = new NioScxTCPServerImpl(new ScxTCPServerOptions().port(8899).tls(tls));
        var i = new AtomicInteger(0);
        tcpServer.onConnect(c -> {
            System.out.println("客户端连接了 !!!");
            int n = i.getAndIncrement();
            var dataReader = new LinkedDataReader(() -> {
                try {
                    return c.read(8192);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            while (true) {
                try {
                    var s = dataReader.readUntil("\r\n".getBytes());
                    if (s == null) {
                        c.close();
                        tcpServer.stop();
                        return;
                    }
                    System.out.println(n + " " + c.getRemoteAddress() + " : " + new String(s));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        tcpServer.start();
        System.out.println("已监听端口号 : " + tcpServer.port());
    }

    public static void test2() throws InterruptedException, IOException {
        var tcpClient = new ScxTCPClientImpl(new ScxTCPClientOptions().tls(tls));
        var tcpSocket = tcpClient.connect(new InetSocketAddress(8899));
        try {
            AtomicInteger i = new AtomicInteger(0);
            while (i.get() < 100) {
                tcpSocket.write((i.getAndIncrement() + "\r\n").getBytes());
                sleep(50);
            }
            tcpSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
