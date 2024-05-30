package cool.scx.logging.test;

import cool.scx.logging.ScxLogRecord;
import cool.scx.logging.ScxLoggerConfig;
import cool.scx.logging.ScxLoggerFactory;
import cool.scx.logging.recorder.ConsoleRecorder;
import cool.scx.logging.recorder.FileRecorder;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.nio.file.Path;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.ERROR;

public class ScxLoggerTest {

    public static void main(String[] args) {
        test1();
    }

    @Test
    public static void test1() {
        var logger = LoggerFactory.getLogger("test1");
        for (int i = 0; i < 10; i++) {
            logger.debug("测试 debug {}", i);
            logger.error("测试 error {}", i);
            logger.error("测试 {}", i, new RuntimeException("错误"));
        }
        var path = getAppRoot();
        ScxLoggerFactory.getLogger("test1").config()
                .setLevel(DEBUG)
                .addRecorder(
                        new ConsoleRecorder() {
                            @Override
                            public String format(ScxLogRecord c) {
                                return c.loggerName() + " : " + c.message() + System.lineSeparator();
                            }
                        },
                        new FileRecorder(path)
                )
                .setStackTrace(true);
        for (int i = 0; i < 10; i++) {
            logger.debug("测试 debug {}", i);
        }
        ScxLoggerFactory.setConfig("test.*", new ScxLoggerConfig().setLevel(ERROR));
        logger.debug("不应该显示出来");
        logger.error("应该显示出来");
    }

    public static Path getAppRoot() {
        try {
            return Path.of(ScxLoggerTest.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (Exception e) {
            return null;
        }
    }

}
