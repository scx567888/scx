package cool.scx.net.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class A {

    public static void main(String[] args) throws IOException {
        var inputStream = new ByteArrayInputStream("Hello world! This is a test input stream.".getBytes());
        var scxInputStream = new LinkedBufferedInputStream(inputStream);
//        var result = scxInputStream.find("world".getBytes());
//        System.out.println(result);
//        byte[] aresult = scxInputStream.readMatch("test".getBytes());
//        System.out.println(new String(result)); // 输出 "Hello "
//        System.out.println(new String(aresult)); // 输出 "Hello "

        System.out.println(new String(scxInputStream.readAllBytes()));
    }
}

