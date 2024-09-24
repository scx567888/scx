package cool.scx.scheduling;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

/**
 * 调度任务
 */
public interface ScheduleTask {

    /**
     * 是否运行并发执行 
     * 运行并发执行的时候 当到达规定时间时 无论上一次任务是否结束 都会开启下一次任务
     *
     * @param concurrent 并发执行
     * @return self
     */
    ScheduleTask concurrent(boolean concurrent);

    /**
     * 最大运行次数 (此参数不受并发影响)
     * 注意 当 并行(concurrent) 设置为 false 的时候 也可以在任务内部 取消 如下
     * <pre>{@code
     *        ScxScheduling
     *                 .fixedRate()
     *                 .delay(Duration.ofMillis(1))
     *                 .concurrent(false)
     *                 .start(c -> {
     *                     if (c.runCount() > 10){
     *                         c.cancel();
     *                     }
     *                     System.err.println(" runCount : " + c.runCount());
     *                 });
     *  }</pre>
     * 但是此方式在 允许并发时并不准确 runCount 计数会因为多个线程同时运行累加从而跳过判断条件
     * 所以如果需要 限制最大运行次数 推荐使用此参数
     *
     * @param maxRunCount 最大运行次数
     * @return self
     */
    ScheduleTask maxRunCount(long maxRunCount);

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
