package cool.scx.scheduling;

public interface CronTask extends ScheduleTask {

    /**
     * cron 表达式
     *
     * @param expression cron 表达式
     * @return self
     */
    CronTask expression(String expression);

    @Override
    default CronTask expirationPolicy(ExpirationPolicy expirationPolicy) {
        //不支持直接跳过
        return this;
    }

}
