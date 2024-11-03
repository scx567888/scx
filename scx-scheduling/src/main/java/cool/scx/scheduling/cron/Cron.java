package cool.scx.scheduling.cron;

import java.time.LocalDateTime;

public class Cron {
    private final CronSecond second;
    private final CronMinute minute;
    private final CronHour hour;
    private final Object dayOfMonth;
    private final CronMonth month;
    private final Object dayOfWeek;

    public Cron(String second, String minute, String hour, String dayOfMonth, String month, String dayOfWeek) {
        this.second = new CronSecond(second);
        this.minute = new CronMinute(minute);
        this.hour = new CronHour(hour);
        this.dayOfMonth = null;
        this.month = new CronMonth(month);
        this.dayOfWeek = null;
    }

    public static Cron of(String cron) {
        var parts = cron.split("\\s+");
        if (parts.length == 5) {
            return new Cron("0", parts[0], parts[1], parts[2], parts[3], parts[4]);
        } else if (parts.length == 6) {
            return new Cron(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        }
        throw new IllegalArgumentException("cron 表达式不正确 : " + cron);
    }

    public LocalDateTime nextTime(LocalDateTime currentTime) {
        return null;
    }

}
