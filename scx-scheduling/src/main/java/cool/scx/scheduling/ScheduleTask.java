package cool.scx.scheduling;

import cool.scx.functional.ScxConsumer;
import cool.scx.timer.ScxTimer;

import java.util.function.Consumer;

/// 调度任务
///
/// @author scx567888
/// @version 0.0.1
public interface ScheduleTask<T extends ScheduleTask<T>> {

    /// 并发策略
    T concurrencyPolicy(ConcurrencyPolicy concurrencyPolicy);

    /// 最大运行次数
    T maxRunCount(long maxRunCount);

    /// 过期策略
    T expirationPolicy(ExpirationPolicy expirationPolicy);

    /// 定时器
    T timer(ScxTimer timer);

    /// 设置任务
    T task(ScxConsumer<TaskContext, ?> task);

    /// 设置错误处理器
    T onError(Consumer<Throwable> errorHandler);

    /// 启动任务
    ScheduleContext start();

    /// 直接启动任务
    default <E extends Throwable> ScheduleContext start(ScxConsumer<TaskContext, E> task) {
        return task(task).start();
    }

}
