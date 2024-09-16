package cool.scx.common.scheduler;

import cool.scx.common.util.ScxVirtualThreadFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * Scx 核心调度器 只是 同时代理了 {@link ScheduledExecutorService} 和 {@link org.springframework.scheduling.TaskScheduler}
 *
 * <br>
 * 同时实现了一些支持自我取消和固定次数的 任务调度
 *
 * @author scx567888
 * @version 2.4.8
 */
public final class ScxScheduler {

    private final ScheduledExecutorService scheduledExecutorService;
    private final TaskScheduler taskScheduler;

    public ScxScheduler() {
        this.scheduledExecutorService = newScheduledThreadPool(Integer.MAX_VALUE, new ScxVirtualThreadFactory());
        this.taskScheduler = new ConcurrentTaskScheduler(this.scheduledExecutorService);
    }

    public ScheduleStatus schedule(Consumer<ScheduleStatus> task, ScheduleOptions options) {
        return new ScheduleRunnable(task).schedule(this.taskScheduler, options);
    }

    public ScheduledExecutorService scheduledExecutorService() {
        return scheduledExecutorService;
    }

}
