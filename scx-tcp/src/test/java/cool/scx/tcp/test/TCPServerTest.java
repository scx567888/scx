package cool.scx.tcp.test;

import cool.scx.byte_reader.ByteReader;
import cool.scx.byte_reader.supplier.InputStreamByteSupplier;
import cool.scx.tcp.TCPServer;

import java.io.IOException;

public class TCPServerTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        var tcpServer = new TCPServer();

        tcpServer.onConnect(c -> {
            System.out.println("客户端连接了 !!!");

            var dataReader = new ByteReader(new InputStreamByteSupplier(c.inputStream()));
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
        tcpServer.start(8899);
        System.out.println("已监听端口号 : " + tcpServer.localAddress().getPort());
    }

}
