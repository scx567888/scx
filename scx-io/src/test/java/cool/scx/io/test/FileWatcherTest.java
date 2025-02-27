package cool.scx.io.test;

import cool.scx.io.file.FileWatcher;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;

public class FileWatcherTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    @Test
    public static void test1() throws IOException {
        var w = new FileWatcher(getAppRoot());

        w.listener(c -> {
            System.out.println(c.type() + " -> " + c.target());
        });

        w.start();

    }

    public static Path getAppRoot() {
        try {
            return Path.of(FileWatcherTest.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (Exception e) {
            return null;
        }
    }

}
