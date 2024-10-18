package cool.scx.net.test;

import cool.scx.io.LinkedDataReader;
import cool.scx.net.*;
import cool.scx.net.tls.TLS;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerTest {

   public static TLS tls;

    static {
        tls = null;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
//        test1();
        test2();
    }

    public static void test1() {
        var tcpServer = new ScxTCPServerImpl(new ScxTCPServerOptions().port(8899).tls(tls));
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

    public static void test2() {
        var tcpServer = new ScxTCPServerImpl(new ScxTCPServerOptions().port(8899).tls(tls));
        var i = new AtomicInteger(0);
        tcpServer.onConnect(c -> {
            System.out.println("客户端连接了 !!!");
            long l = System.nanoTime();
            var path = Path.of("C:\\Users\\scx\\Documents\\ISO\\ubuntu-24.04.1-desktop-amd64.iso");
            try {
                c.sendFile(path,0, Files.size(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("耗时 :" +(System.nanoTime() - l)/1000_000);
            try {
                c.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        tcpServer.start();
        System.out.println("已监听端口号 : " + tcpServer.port());
    }

}
