package cool.scx.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * 根据 Key 进行锁 , 使用 Semaphore 以支持多线程的 访问
 *
 * @param <T> KEY
 */
public final class LockByKey<T> {

    private final ConcurrentHashMap<T, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

    private final Function<T, Semaphore> semaphoreBuilder;

    public LockByKey() {
        this((k) -> new Semaphore(1, true));
    }

    public LockByKey(Function<T, Semaphore> semaphoreBuilder) {
        this.semaphoreBuilder = semaphoreBuilder;
    }

    public void lock(T key) {
        var semaphore = semaphoreMap.computeIfAbsent(key, semaphoreBuilder);
        semaphore.acquireUninterruptibly();
    }

    public void unlock(T key) {
        var semaphore = semaphoreMap.get(key);
        if (semaphore != null) {
            semaphore.release();
            if (semaphore.getQueueLength() == 0) {
                semaphoreMap.remove(key, semaphore);
            }
        }
    }

}
