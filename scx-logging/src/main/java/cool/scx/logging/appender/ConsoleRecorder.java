package cool.scx.logging.appender;

import cool.scx.logging.ScxLogEvent;
import cool.scx.logging.ScxLogRecorder;
import cool.scx.logging.ScxLoggingLevel;

/**
 * 控制台 记录器
 */
public class ConsoleRecorder implements ScxLogRecorder {

    @Override
    public void record(ScxLogEvent event) {
        var data = getLayout().format(event);
        //错误级别的我们就采用 err 打印 其余的 正常输出
        if (event.level().toInt() <= ScxLoggingLevel.ERROR.toInt()) {
            System.err.print(data);
        } else {
            System.out.print(data);
        }
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    public static final ConsoleRecorder CONSOLE_RECORDER = new ConsoleRecorder();

}
