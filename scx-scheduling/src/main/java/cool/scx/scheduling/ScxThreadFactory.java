package cool.scx.scheduling;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * 专用于调度器的线程工厂 第一次会返回一个平台线程 (防止退出) 之后会返回虚拟线程
 */
final class ScxThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private final AtomicLong threadNumber = new AtomicLong(0);
    private final String namePrefix;
    private Function<Runnable, Thread> newThread;

    public ScxThreadFactory() {
        this.namePrefix = "scx-" + POOL_NUMBER.getAndIncrement() + "-virtual-thread-";
        // 只有第一次返回一个平台线程 剩余都返回虚拟线程
        this.newThread = (r) -> {
            this.newThread = (c) -> Thread.ofVirtual().name(namePrefix, threadNumber.getAndIncrement()).unstarted(c);
            return Thread.ofPlatform().name(namePrefix, threadNumber.getAndIncrement()).unstarted(r);
        };
    }

    @Override
    public Thread newThread(Runnable r) {
        return newThread.apply(r);
    }

}
