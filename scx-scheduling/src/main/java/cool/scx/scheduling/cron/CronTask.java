package cool.scx.scheduling.cron;


import cool.scx.scheduling.ExpirationPolicy;
import cool.scx.scheduling.ScheduleTask;

/// Cron 执行的任务
///
/// @author scx567888
/// @version 0.0.1
public interface CronTask extends ScheduleTask {

    /// cron 表达式
    ///
    /// @param expression cron 表达式
    /// @return self
    CronTask expression(String expression);

    @Override
    default CronTask expirationPolicy(ExpirationPolicy expirationPolicy) {
        //不支持直接跳过
        return this;
    }

}
