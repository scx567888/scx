package cool.scx.logging.recorder;

import cool.scx.logging.ScxLogRecord;
import cool.scx.logging.ScxLogRecorder;
import cool.scx.logging.ScxLoggingLevel;

import static cool.scx.logging.ScxLoggingLevel.*;

/**
 * 控制台 记录器
 */
public class ConsoleRecorder extends ScxLogRecorder {

    @Override
    public void record(ScxLogRecord logRecord) {
        var data = formatter().format(logRecord);
        //错误级别的我们就采用 err 打印 其余的 正常输出
        if (logRecord.level().intLevel() <= ERROR.intLevel()) {
            System.err.print(data);
        } else {
            System.out.print(data);
        }
    }

    @Override
    public String name() {
        return "CONSOLE";
    }

}
