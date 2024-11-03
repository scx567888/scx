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
        String[] parts = cron.split(" ");
        if (parts.length == 5) {
            return new Cron("0", parts[0], parts[1], parts[2], parts[3], parts[4]);
        } else if (parts.length == 6) {
            return new Cron(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
        } else {
            throw new IllegalArgumentException("cron 表达式不正确 : " + cron);
        }
    }

    public LocalDateTime nextTime(LocalDateTime currentTime) {
        LocalDateTime nextTime = currentTime.withNano(0);
        boolean secondUpdated = false;
        int[] var4 = this.second.values;
        int var5 = var4.length;

        int var6;
        int s;
        for(var6 = 0; var6 < var5; ++var6) {
            s = var4[var6];
            if (s > nextTime.getSecond()) {
                nextTime = nextTime.withSecond(s);
                secondUpdated = true;
                break;
            }
        }

        if (!secondUpdated) {
            nextTime = nextTime.plusMinutes(1L).withSecond(this.second.values[0]);
        }

        boolean minuteUpdated = false;
        int[] var11 = this.minute.values;
        var6 = var11.length;

        int m;
        for(s = 0; s < var6; ++s) {
            m = var11[s];
            if (m >= nextTime.getMinute()) {
                nextTime = nextTime.withMinute(m);
                minuteUpdated = true;
                break;
            }
        }

        if (!minuteUpdated) {
            nextTime = nextTime.plusHours(1L).withMinute(this.minute.values[0]).withSecond(this.second.values[0]);
        }

        boolean hourUpdated = false;
        int[] var13 = this.hour.values;
        s = var13.length;

        for(m = 0; m < s; ++m) {
            int h = var13[m];
            if (h >= nextTime.getHour()) {
                nextTime = nextTime.withHour(h);
                hourUpdated = true;
                break;
            }
        }

        if (!hourUpdated) {
            nextTime = nextTime.plusDays(1L).withHour(this.hour.values[0]).withMinute(this.minute.values[0]).withSecond(this.second.values[0]);
        }

        return nextTime;
    }
    
}
