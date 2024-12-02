package cool.scx.scheduling;

public interface CronTask extends ScheduleTask {

    CronTask expression(String expression);

}
