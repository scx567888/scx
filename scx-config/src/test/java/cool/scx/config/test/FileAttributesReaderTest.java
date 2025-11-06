package cool.scx.config.test;

import cool.scx.config.helper.FileAttributesReader;

import java.io.IOException;
import java.nio.file.Path;

public class FileAttributesReaderTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        var s = new FileAttributesReader(Path.of("xxxx"));
        var p = Path.of("xxxx/xxxx");
        var l = System.nanoTime();
        for (int i = 0; i < 99999; i = i + 1) {
            var basicFileAttributes = s.get(p);
            if (basicFileAttributes != null) {
                System.out.println(basicFileAttributes.size());
            } else {
                System.out.println("未找到");
            }
        }
        System.out.println((System.nanoTime() - l) / 1000_000);
    }

}
