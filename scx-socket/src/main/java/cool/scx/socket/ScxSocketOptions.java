package cool.scx.socket;

import cool.scx.scheduling.ScxScheduler;

import java.util.concurrent.Executor;

public class ScxSocketOptions {

    /**
     * 重复帧检查器清除超时
     */
    private int duplicateFrameCheckerClearTimeout;

    private Executor executor;

    public ScxSocketOptions() {
        this.duplicateFrameCheckerClearTimeout = 1000 * 60 * 10;// 默认 10 分钟
        this.executor = ScxScheduler.getInstance();
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

    public ScxSocketOptions executor(Executor executor) {
        this.executor = executor;
        return this;
    }

}
