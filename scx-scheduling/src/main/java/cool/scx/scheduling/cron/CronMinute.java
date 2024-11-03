package cool.scx.scheduling.cron;

import static cool.scx.scheduling.cron.CronHelper.normalize;
import static cool.scx.scheduling.cron.CronHelper.parseSegment;

public class CronMinute {

    public final int[] values;
    public final Type type;

    public CronMinute(String str) {
        var result = parseSegment(str, 0, 59);
        this.values = normalize(result.values);
        this.type = result.type;
    }

}
