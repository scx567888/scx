package cool.scx.net.test;

import cool.scx.net.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;

import static cool.scx.net.test.ServerTest.tls;
import static java.lang.Thread.sleep;

public class ClientTest {

    public static void main(String[] args) throws InterruptedException, IOException {
//        test1();
        for (int i = 0; i < 1; i++) {
            Thread.ofVirtual().start(()->{
                test2();    
            });
                
        }
        Thread.sleep(9999999);
    }

    public static void test1() throws InterruptedException, IOException {
        var tcpClient = new ScxTCPClientImpl(new ScxTCPClientOptions().tls(tls));
        var tcpSocket = tcpClient.connect(new InetSocketAddress(8899));
        try {
            AtomicInteger i = new AtomicInteger(0);
            while (i.get() < 100) {
                tcpSocket.sendFile((i.getAndIncrement() + "\r\n").getBytes());
//                sleep(50);
            }
            tcpSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test2() {
        var tcpClient = new NioScxTCPClientImpl(new ScxTCPClientOptions().tls(tls));
        var tcpSocket = tcpClient.connect(new InetSocketAddress(8899));
        try {
            try (var f= FileChannel.open(Path.of("C:\\Users\\scx\\Desktop\\aaaa.iso"), StandardOpenOption.CREATE, StandardOpenOption.WRITE);) {
                var nnn= (NioScxTCPSocketImpl)tcpSocket;
                long l = f.transferFrom(nnn.socketChannel(), 0, Long.MAX_VALUE);
                System.out.println();
            }
            
//            while (true) {
//
//                byte[] read = tcpSocket.read(8192);
////                System.out.println(read.length);
//            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
