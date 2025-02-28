package cool.scx.scheduling;

import java.util.concurrent.ScheduledExecutorService;
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

    /// 执行器
    T executor(ScheduledExecutorService executor);

    /// 设置任务
    T task(Task task);
    
    /// 设置错误处理器
    T onError(Consumer<Throwable> errorHandler);

    /// 启动任务
    ScheduleContext start();

    /// 直接启动任务
    default ScheduleContext start(Task task) {
        return task(task).start();
    }

}
