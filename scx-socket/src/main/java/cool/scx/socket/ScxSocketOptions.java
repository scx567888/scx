package cool.scx.socket;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScxSocketOptions {

    /**
     * 重复帧检查器清除超时
     */
    private int duplicateFrameCheckerClearTimeout;

    private ScheduledExecutorService executor;

    public ScxSocketOptions() {
        this.duplicateFrameCheckerClearTimeout = 1000 * 60 * 10;// 默认 10 分钟
        this.executor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    public int getDuplicateFrameCheckerClearTimeout() {
        return duplicateFrameCheckerClearTimeout;
    }

    public ScxSocketOptions setDuplicateFrameCheckerClearTimeout(int duplicateFrameCheckerClearTimeout) {
        this.duplicateFrameCheckerClearTimeout = duplicateFrameCheckerClearTimeout;
        return this;
    }

    public ScheduledExecutorService executor() {
        return executor;
    }

    public ScxSocketOptions executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

}
