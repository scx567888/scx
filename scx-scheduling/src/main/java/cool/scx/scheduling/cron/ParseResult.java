package cool.scx.scheduling.cron;

public class ParseResult {

    public int[] values;
    public Type type;

    public ParseResult(Type type, int... values) {
        this.values = values;
        this.type = type;
    }

}
