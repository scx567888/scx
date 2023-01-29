package cool.scx.logging;

import cool.scx.logging.layout.DefaultLayout;

/**
 * 日志记录器
 */
public interface ScxLogRecorder {

    void record(ScxLogEvent event);

    String getName();

    default ScxLogLayout getLayout() {
        return DefaultLayout.DEFAULT_LAYOUT;
    }

}
