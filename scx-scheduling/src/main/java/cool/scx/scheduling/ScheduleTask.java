package cool.scx.scheduling;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * 调度任务
 */
public interface ScheduleTask {

    ScheduleTask concurrentExecution(boolean concurrentExecution);

    ScheduleTask executor(ScheduledExecutorService executor);

    ScheduleTask task(Consumer<ScheduleStatus> task);

    ScheduleStatus start();

    default ScheduleStatus start(Consumer<ScheduleStatus> task) {
        return task(task).start();
    }

}
