package cool.scx.core;

public class ScxThread extends Thread {

    private final ScxThreadFactory scxThreadFactory;

    public ScxThread(ThreadGroup group, Runnable r, String s, int i, ScxThreadFactory scxThreadFactory) {
        super(group, r, s, i);
        this.scxThreadFactory = scxThreadFactory;
    }

    public ScxThreadFactory scxThreadFactory() {
        return scxThreadFactory;
    }

    public Scx scx() {
        return scxThreadFactory.scx();
    }

}
