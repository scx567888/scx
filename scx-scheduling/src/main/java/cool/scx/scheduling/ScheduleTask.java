package cool.scx.scheduling;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * 调度任务
 */
public interface ScheduleTask {

    /**
     * 过期策略
     *
     * @param expirationPolicy a
     * @return a
     */
    ScheduleTask expirationPolicy(ExpirationPolicy expirationPolicy);

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

    /**
     * 任务
     *
     * @param task 任务
     * @return self
     */
    ScheduleTask task(Consumer<ScheduleStatus> task);

    /**
     * 启动任务
     *
     * @return 调度状态
     */
    ScheduleStatus start();

    default ScheduleStatus start(Consumer<ScheduleStatus> task) {
        return task(task).start();
    }

}
