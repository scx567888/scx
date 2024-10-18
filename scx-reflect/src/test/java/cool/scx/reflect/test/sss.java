package cool.scx.reflect.test;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class sss {

    public static void main(String[] args) throws IOException {
        Path path = Path.of("C:\\Users\\scx\\Desktop\\aaaa.iso");
        for (int i = 0; i < 999; i++) {
            long size = Files.size(path);
        }
        var s=System.nanoTime();
        for (int i = 0; i < 999; i++) {
            long size = Files.size(path);
        }
        System.out.println((System.nanoTime()-s)/1000_000);

        FileChannel open = FileChannel.open(path);
        var s1=System.nanoTime();
        for (int i = 0; i < 999; i++) {
            long size = open.size();
        }
        System.out.println((System.nanoTime()-s1)/1000_000);
    }
}
