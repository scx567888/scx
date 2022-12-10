package cool.scx.test;

import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.ScxLoggingLevel;
import cool.scx.logging.ScxLoggingType;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.nio.file.Path;

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
        ScxLoggerFactory.updateLogger(ScxLoggerTest.class, ScxLoggingLevel.DEBUG, ScxLoggingType.BOTH, path, true);
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
