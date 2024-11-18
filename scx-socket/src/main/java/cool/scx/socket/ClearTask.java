package cool.scx.socket;

import cool.scx.common.util.$.Timeout;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static cool.scx.common.util.$.setTimeout;
import static cool.scx.socket.DuplicateFrameChecker.Key;

final class ClearTask {

    private final DuplicateFrameChecker checker;
    private final Key key;
    private Timeout clearTimeout;
    private final Lock lock = new ReentrantLock();

    public ClearTask(Key key, DuplicateFrameChecker checker) {
        this.key = key;
        this.checker = checker;
    }

    public void start() {
        lock.lock();
        try {
            cancel();
            this.clearTimeout = setTimeout(this::clear, this.checker.getClearTimeout());
        } finally {
            lock.unlock();
        }
    }

    public void cancel() {
        lock.lock();
        try {
            if (this.clearTimeout != null) {
                this.clearTimeout.cancel();
                this.clearTimeout = null;
            }
        } finally {
            lock.unlock();
        }
    }

    private void clear() {
        this.checker.clearTaskMap.remove(key);
    }

}
