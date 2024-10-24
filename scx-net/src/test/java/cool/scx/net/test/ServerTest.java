package cool.scx.net.test;

import cool.scx.io.LinkedDataReader;
import cool.scx.net.ScxTCPServerOptions;
import cool.scx.net.TCPServer;
import cool.scx.net.tls.TLS;

import java.io.IOException;

public class ServerTest {

    public static TLS tls;

    static {
        tls = null;
    }

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var tcpServer = new TCPServer(new ScxTCPServerOptions().port(8899).tls(tls));

        tcpServer.onConnect(c -> {
            System.out.println("客户端连接了 !!!");

            var dataReader = new LinkedDataReader(() -> {
                try {
                    return new LinkedDataReader.Node(c.read(8192));
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
                    System.out.println(c.remoteAddress() + " : " + new String(s));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        tcpServer.start();
        System.out.println("已监听端口号 : " + tcpServer.port());
    }

}
