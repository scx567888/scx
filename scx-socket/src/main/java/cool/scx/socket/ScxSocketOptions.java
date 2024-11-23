package cool.scx.socket;

import java.util.concurrent.*;

public class ScxSocketOptions {

    /**
     * 重复帧检查器清除超时
     */
    private int duplicateFrameCheckerClearTimeout;

    /**
     * 调度器 : 用于消息重发, 心跳检测等
     */
    private ScheduledExecutorService scheduledExecutor;

    /**
     * 执行器 : 用于执行 onMessage onEvent 之类的回调
     */
    private Executor executor;

    public ScxSocketOptions() {
        // 默认 10 分钟
        this.duplicateFrameCheckerClearTimeout = 1000 * 60 * 10;
        // 默认 cpu 核心数 2倍
        this.scheduledExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2);
        // 默认 无限大
        this.executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
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

    public ScxSocketOptions executor(Executor executor) {
        this.executor = executor;
        return this;
    }

    public ScxSocketOptions scheduledExecutor(ScheduledExecutorService scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
        return this;
    }

}
