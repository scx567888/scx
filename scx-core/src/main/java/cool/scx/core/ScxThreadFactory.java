package cool.scx.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ScxThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private final Scx scx;

    ScxThreadFactory(Scx scx) {
        this.scx = scx;
        this.group = Thread.currentThread().getThreadGroup();
        this.namePrefix = "scx-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new ScxThread(group, r, namePrefix + threadNumber.getAndIncrement(), 0, this);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

    public Scx scx() {
        return scx;
    }

}
