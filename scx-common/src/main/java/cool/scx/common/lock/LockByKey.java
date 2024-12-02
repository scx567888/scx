package cool.scx.common.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 根据 Key 进行锁 , 使用 Semaphore 以支持多线程的 访问
 *
 * @param <T> KEY
 * @author scx567888
 * @version 0.0.1
 */
public final class LockByKey<T> implements ILockByKey<T> {

    private final ConcurrentHashMap<T, LockWrapper> lockMap = new ConcurrentHashMap<>();

    private final Function<T, Semaphore> semaphoreBuilder;

    public LockByKey() {
        this((k) -> new Semaphore(1, true));
    }

    public LockByKey(Function<T, Semaphore> semaphoreBuilder) {
        this.semaphoreBuilder = semaphoreBuilder;
    }

    @Override
    public void lock(T key) {
        var l = lockMap.computeIfAbsent(key, (k) -> new LockWrapper(semaphoreBuilder.apply(k)));
        l.queueLength.incrementAndGet();
        l.lock.acquireUninterruptibly();
    }

    @Override
    public void unlock(T key) {
        var l = lockMap.get(key);
        if (l != null) {
            l.lock.release();
            if (l.queueLength.decrementAndGet() == 0) {
                lockMap.remove(key, l);
            }
        }
    }

    private static class LockWrapper {

        private final Semaphore lock;

        /**
         * 因为 Semaphore 的 getQueueLength() 不保证并发 所以这里使用 单独的计数器
         */
        private final AtomicInteger queueLength;

        private LockWrapper(Semaphore lock) {
            this.lock = lock;
            this.queueLength = new AtomicInteger(0);
        }

    }

}
