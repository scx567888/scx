package cool.scx.tcp.test;

import cool.scx.io.data_reader.LinkedDataReader;
import cool.scx.io.data_supplier.InputStreamDataSupplier;
import cool.scx.tcp.NioTCPServer;
import cool.scx.tcp.ScxTCPServerOptions;
import cool.scx.tcp.tls.TLS;

public class TCPServerTest {

    public static TLS tls;

    static {
        tls = null;
    }

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        var tcpServer = new NioTCPServer(new ScxTCPServerOptions().port(8899).tls(tls));

        tcpServer.onConnect(c -> {
            System.out.println("客户端连接了 !!!");

            var dataReader = new LinkedDataReader(new InputStreamDataSupplier(c.inputStream()));
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
                    break;
                }
            }
            System.err.println("完成");
        });
        tcpServer.start();
        System.out.println("已监听端口号 : " + tcpServer.localAddress().getPort());
    }

}
