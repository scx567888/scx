package cool.scx.scheduling.cron;

import static cool.scx.scheduling.cron.CronHelper.normalize;
import static cool.scx.scheduling.cron.CronHelper.parseSegment;

public class CronMonth {

    public final int[] values;
    public final Type type;

    public CronMonth(String str) {
        var result = parseSegment(str, 1, 12);
        this.values = normalize(result.values);
        this.type = result.type;
    }

}
