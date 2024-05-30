package cool.scx.logging.recorder;

import cool.scx.logging.ScxLogRecord;

import static java.lang.System.Logger.Level.ERROR;

/**
 * 控制台 记录器
 */
public class ConsoleRecorder extends AbstractRecorder {

    @Override
    public void record0(ScxLogRecord logRecord) {
        var data = format(logRecord);
        //错误级别的我们就采用 err 打印 其余的 正常输出
        if (logRecord.level().getSeverity() >= ERROR.getSeverity()) {
            System.err.print(data);
        } else {
            System.out.print(data);
        }
    }

}
