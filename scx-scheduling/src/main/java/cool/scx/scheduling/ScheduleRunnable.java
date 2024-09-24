package cool.scx.scheduling;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.function.Consumer;

/**
 * 带有计数器的 Runnable 内部包含一个被调用次数的计数器
 *
 * @author scx567888
 * @version 1.11.8
 */
class ScheduleRunnable implements Runnable {

    /**
     * 执行 scxHandler
     */
    final Consumer<ScheduleStatus> scxHandler;
    private ScheduleStatus scheduleStatus;

    public ScheduleRunnable(Consumer<ScheduleStatus> scxHandler) {
        this.scxHandler = scxHandler;
    }

    @Override
    public void run() {
        scxHandler.accept(scheduleStatus.addRunCount());
    }

    public ScheduleStatus schedule(TaskScheduler taskScheduler, ScheduleOptions options) {
        var scheduledFuture = switch (options) {
            case CronScheduleOptions c -> taskScheduler.schedule(this, new CronTrigger(c.getCron()));
            case OnceScheduleOptions o -> taskScheduler.schedule(this, o.getStartTime());
            case FixedDelayScheduleOptions f ->
                    taskScheduler.scheduleWithFixedDelay(this, f.getStartTime(), f.getDelay());
            case FixedRateScheduleOptions f -> taskScheduler.scheduleAtFixedRate(this, f.getStartTime(), f.getDelay());
        };
        this.scheduleStatus = new ScheduleStatus(scheduledFuture);
        return this.scheduleStatus;
    }

}
