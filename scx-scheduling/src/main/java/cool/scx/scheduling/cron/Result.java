package cool.scx.scheduling.cron;

class Result {
    
    public int[] values;
    public Type type;

    public Result(Type type, int... values) {
        this.values = values;
        this.type = type;
    }
    
}
