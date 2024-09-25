package cool.scx.scheduling;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * 调度任务
 */
public interface ScheduleTask {

    /**
     * 执行器
     * <p>
     * 默认会使用单例的 ScxScheduler
     * 不建议自行设置
     *
     * @param executor 执行器
     * @return self
     */
    ScheduleTask executor(ScheduledExecutorService executor);

    ScheduleTask task(Consumer<ScheduleStatus> task);

    ScheduleStatus start();

    default ScheduleStatus start(Consumer<ScheduleStatus> task) {
        return task(task).start();
    }

}
