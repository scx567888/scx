package cool.scx.test;

import cool.scx.logging.ScxLoggerFactory;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.nio.file.Path;

import static cool.scx.logging.ScxLoggingLevel.DEBUG;
import static cool.scx.logging.ScxLoggingType.BOTH;

public class ScxLoggerTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var logger = LoggerFactory.getLogger(ScxLoggerTest.class);
        for (int i = 0; i < 99; i++) {
            logger.debug("测试 debug {}", i);
            logger.error("测试 error {}", i);
            logger.error("测试 {}", i, new RuntimeException("错误"));
        }
        var path = getResourcePath();
        ScxLoggerFactory.getLogger(ScxLoggerTest.class).setLevel(DEBUG).setType(BOTH).setStoredDirectory(path).setStackTrace(true);
        for (int i = 0; i < 99; i++) {
            logger.debug("测试 debug {}", i);
        }
    }

    public static Path getResourcePath() {
        try {
            return Path.of(ScxLoggerTest.class.getResource("/").toURI());
        } catch (Exception e) {
            return null;
        }
    }

}
