package cool.scx.scheduling.cron;

import static cool.scx.scheduling.cron.CronHelper.normalize;
import static cool.scx.scheduling.cron.CronHelper.parseSegment;

public class CronHour {

    public final int[] values;
    public final Type type;

    public CronHour(String str) {
        var result = parseSegment(str, 0, 23);
        this.values = normalize(result.values);
        this.type = result.type;
    }

}
