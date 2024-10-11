package cool.scx.io.test.t;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class A {

    public static void main(String[] args) throws IOException {
        var inputStream = new ByteArrayInputStream("Hello world! This is a test input stream.".getBytes());
        var scxInputStream = new LinkedBufferedInputStream(inputStream);
        var s = scxInputStream.first;
        while (s != null) {
            System.out.println(new String(s.bytes));
//            s.setFirst();
            s = s.readNext();
        }
        var w = scxInputStream.first;
        while (w != null) {
            System.out.println(new String(w.bytes));
            w.setFirst();
            w = w.next();
        }
        System.out.println();
//        Iterator<LinkedBufferedInputStream.Node> iterator = scxInputStream.iterator();
//        while (iterator.hasNext()) {
//            var node = iterator.next();
//            System.out.println(new String(node.bytes));
//        }
//        int i = scxInputStream.find((byte) 111);
//        var result = scxInputStream.find("world".getBytes());
//        System.out.println(result);
//        byte[] aresult = scxInputStream.readMatch("test".getBytes());
//        System.out.println(new String(result)); // 输出 "Hello "
//        System.out.println(new String(aresult)); // 输出 "Hello "

//        System.out.println(new String(scxInputStream.readAllBytes()));
    }
}

