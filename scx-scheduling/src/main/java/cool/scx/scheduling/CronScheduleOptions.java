package cool.scx.scheduling;

public non-sealed class CronScheduleOptions implements ScheduleOptions {

    private final String cron;

    public CronScheduleOptions(String cron) {
        this.cron = cron;
    }

    public String getCron() {
        return cron;
    }

}
