package cool.scx.io.test.t;

import cool.scx.common.util.ArrayUtils;
import io.helidon.common.buffers.DataReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class Test1 {

    static byte[] aaa = "\r\n".getBytes();

    public static void main(String[] args) throws IOException {
        for (int j = 0; j < 20; j++) {
            long l = System.nanoTime();
            for (int i = 0; i < 10000; i++) {
//            String s = test1();
//                var s1 = test2();
//            String s11 = test3();
//            String s11 = test4();
//            String s1a1 = test5();
//            String s32 = test6();
//            var i1 = test7();
//            var i1 = test8();
//            var i1 = test9();
                var i1 = test10();
//            var i11 = test12();
//            System.out.println();
            }
            System.out.println((System.nanoTime() - l) / 1000_000);
        }

    }

    public static String test1() throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));

        var inputStream1 = new InputStream() {
            @Override
            public int read(byte[] b) throws IOException {
//                System.err.println("read1");
                return inputStream.read(b);
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
//                System.err.println("read2");
                return inputStream.read(b, off, len);
            }

            @Override
            public int read() throws IOException {
//                System.err.println("read3");
                return inputStream.read();
            }
        };

//        var b = new ScxInputStream(inputStream1);
//        try (b) {
//            byte[] bytes = b.readMatch( "\r\n".getBytes());
//            return new String(bytes);
////            return null;
//        }
        return null;

    }

    public static int test2() throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));
        DataReader dataReader = new DataReader(new Supplier<byte[]>() {
            @Override
            public byte[] get() {
                try {
                    return inputStream.readNBytes(8192);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return dataReader.findNewLine(Integer.MAX_VALUE);
    }

    public static String test3() throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));
        var s = new BufferedReader(new InputStreamReader(inputStream));
        return s.readLine();
    }

    public static String test4() throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));
        while (true) {
            byte[] bytes = inputStream.readNBytes(8192);
            int i = ArrayUtils.indexOf(bytes, "\r\n".getBytes());
            if (i != -1) {
                return new String(bytes, 0, i);
            }
        }

    }

    public static String test5() throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));
        LineReaderInputStream s = new LineReaderInputStream(inputStream);
        return s.readLine();
    }

    public static String test6() throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));
        InputStreamSearch.indexOf(inputStream, "\r\n".getBytes());
        return "";

    }

    public static String test7() throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));
        DataReader d = new DataReader(() -> {
            try {
                return inputStream.readNBytes(8192);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

//        var i= d.findPattern("\r\n".getBytes(), Integer.MAX_VALUE);
//        return new String(d.readBytes(i));
//        return d.findNewLine( Integer.MAX_VALUE);
        return null;
    }

    public static int test8() throws FileNotFoundException {

        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt");

        int i = ArrayFinder.indexOf(() -> {
            try {
                byte[] bytes = fileInputStream.readNBytes(8192);
                if (bytes.length == 0) {
                    return null;
                } else {
                    return bytes;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, aaa);

        return i;
    }

    public static int test9() throws IOException {

        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));

        LinkedBufferedInputStream sss = new LinkedBufferedInputStream(inputStream);

//        return sss.indexOf("\r\n".getBytes());
        return 1;
    }

    public static int test10() throws IOException {

        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));
//
//        var inputStream=new ByteArrayInputStream("\r\r\r\r\r\r\r\r\r\n".getBytes());

//        var sss = new KMPInputStream(inputStream);
//
//        var s1 = sss.indexOf("\r\n".getBytes());
//        return s1;
        return 1;
    }

    public static int test12() throws IOException {

        var inputStream = new ByteArrayInputStream("\r\r\r\r\r\r\r\r\r\n".getBytes());
        DataReader dataReader = new DataReader(new Supplier<byte[]>() {
            @Override
            public byte[] get() {
                try {
                    return inputStream.readNBytes(8192);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, false);
        return dataReader.findNewLine(Integer.MAX_VALUE);

    }

}
