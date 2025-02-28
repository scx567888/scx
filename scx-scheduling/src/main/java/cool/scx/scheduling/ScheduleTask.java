package cool.scx.scheduling;

import java.util.concurrent.ScheduledExecutorService;

/// 调度任务
///
/// @author scx567888
/// @version 0.0.1
public interface ScheduleTask<T extends ScheduleTask<T>> {

    /// 是否运行并发执行
    /// 运行并发执行的时候 当到达规定时间时 无论上一次任务是否结束 都会开启下一次任务
    ///
    /// @param concurrent 并发执行
    /// @return self
    T concurrent(boolean concurrent);

    /// 最大运行次数 (此参数不受并发影响)
    /// 注意 当 并行(concurrent) 设置为 false 的时候 也可以在任务内部 取消 如下
    /// ```java
    /// ScxScheduling
    ///     .fixedRate()
    ///     .delay(Duration.ofMillis(1))
    ///     .concurrent(false)
    ///     .start(c ->{
    ///         if (c.runCount() > 10){
    ///             c.cancel();
    ///}
    ///         System.err.println(" runCount : " + c.runCount());
    ///});
    ///```
    /// 但是此方式在 允许并发时并不准确 runCount 计数会因为多个线程同时运行累加从而跳过判断条件
    /// 所以如果需要 限制最大运行次数 推荐使用此参数
    ///
    /// @param maxRunCount 最大运行次数
    /// @return self
    T maxRunCount(long maxRunCount);

    /// 过期策略
    ///
    /// @param expirationPolicy a
    /// @return a
    T expirationPolicy(ExpirationPolicy expirationPolicy);

    /// 执行器
    /// 默认会使用单例的 ScxScheduler
    /// 不建议自行设置
    ///
    /// @param executor 执行器
    /// @return self
    T executor(ScheduledExecutorService executor);

    /// 设置任务
    /// 注意 这里任务会获得一个 ScheduleStatus 但是和 start 返回的 ScheduleStatus 不同
    /// 这里的 ScheduleStatus 相当于一个快照 即 不会根据 调度任务本身的状态进行变化
    /// @param task 任务
    /// @return self
    T task(Task task);

    /// 启动任务
    ///
    /// @return 调度状态
    ScheduleContext start();

    /// 直接启动任务
    ///
    /// @return 调度状态
    default ScheduleContext start(Task task) {
        return task(task).start();
    }

}
