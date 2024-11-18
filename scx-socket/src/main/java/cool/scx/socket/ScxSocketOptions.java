package cool.scx.socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScxSocketOptions {

    /**
     * 重复帧检查器清除超时
     */
    private int duplicateFrameCheckerClearTimeout;

    private ScheduledExecutorService scheduledExecutor;
    
    private Executor executor;

    public ScxSocketOptions() {
        this.duplicateFrameCheckerClearTimeout = 1000 * 60 * 10;// 默认 10 分钟
        this.scheduledExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    public int getDuplicateFrameCheckerClearTimeout() {
        return duplicateFrameCheckerClearTimeout;
    }

    public ScxSocketOptions setDuplicateFrameCheckerClearTimeout(int duplicateFrameCheckerClearTimeout) {
        this.duplicateFrameCheckerClearTimeout = duplicateFrameCheckerClearTimeout;
        return this;
    }

    public Executor executor() {
        return executor;
    }

    public ScheduledExecutorService scheduledExecutor() {
        return scheduledExecutor;
    }

    public ScxSocketOptions executor(ScheduledExecutorService executor) {
        this.executor = executor;
        return this;
    }

}
