package cool.scx.scheduling.cron;

public class CronSecond {

    public final int[] values;
    public final Type type;

    public CronSecond(String str) {
        var result = CronHelper.parseSegment(str, 0, 59);
        this.values = CronHelper.normalize(result.values);
        this.type = result.type;
    }
    
}
