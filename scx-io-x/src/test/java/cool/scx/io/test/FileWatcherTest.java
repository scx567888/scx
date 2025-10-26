package cool.scx.io.test;

import cool.scx.common.util.ClassUtils;
import cool.scx.io.x.file.FileWatcher;
import org.testng.annotations.Test;

import java.io.IOException;

public class FileWatcherTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    @Test
    public static void test1() throws IOException {
        var w = new FileWatcher(ClassUtils.getAppRoot(FileWatcherTest.class));

        w.listener(c -> {
            System.out.println(c.type() + " -> " + c.target());
        });

        w.start();

    }

}
