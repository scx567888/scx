package cool.scx.util.test;

import cool.scx.util.FileWatcher;
import cool.scx.util.reflect.ClassUtils;
import org.testng.annotations.Test;

import java.io.IOException;

public class FileWatcherTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    @Test
    public static void test1() throws IOException {
        var w = new FileWatcher(ClassUtils.getAppRoot(FileWatcherTest.class));

        w.onCreate(() -> {
            System.out.println("创建了");
        });

        w.onModify(() -> {
            System.out.println("修改了");
        });

        w.onDelete(() -> {
            System.out.println("删除了");
        });

        w.start();

    }

}
