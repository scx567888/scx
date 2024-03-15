package cool.scx.common.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ScxVirtualThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicLong threadNumber = new AtomicLong(0);
    private final String namePrefix;

    public ScxVirtualThreadFactory() {
        this.namePrefix = "scx-" + poolNumber.getAndIncrement() + "-virtual-thread-";
    }

    public Thread newThread(Runnable r) {
        return Thread.ofVirtual().name(namePrefix, threadNumber.getAndIncrement()).unstarted(r);
    }

}
