package cool.scx.logging.test;

import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.recorder.ConsoleRecorder;
import cool.scx.logging.recorder.FileRecorder;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.nio.file.Path;

import static java.lang.System.Logger.Level.DEBUG;

public class ScxLoggerTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var logger = LoggerFactory.getLogger(ScxLoggerTest.class);
        for (int i = 0; i < 10; i++) {
            logger.debug("测试 debug {}", i);
            logger.error("测试 error {}", i);
            logger.error("测试 {}", i, new RuntimeException("错误"));
        }
        var path = getAppRoot();
        ScxLoggerFactory.getLogger(ScxLoggerTest.class).config()
                .setLevel(DEBUG)
                .addRecorder(
                        new ConsoleRecorder().setFormatter((c) -> c.loggerName() + " : " + c.message() + System.lineSeparator()),
                        new FileRecorder(path)
                )
                .setStackTrace(true);
        for (int i = 0; i < 10; i++) {
            logger.debug("测试 debug {}", i);
        }
    }

    public static Path getAppRoot() {
        try {
            return Path.of(ScxLoggerTest.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (Exception e) {
            return null;
        }
    }

}
