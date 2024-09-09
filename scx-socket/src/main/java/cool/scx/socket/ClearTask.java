package cool.scx.socket;


import cool.scx.common.util.$.Timeout;

import static cool.scx.common.util.$.setTimeout;
import static cool.scx.socket.DuplicateFrameChecker.Key;

final class ClearTask {

    private final DuplicateFrameChecker checker;
    private final Key key;
    private volatile Timeout clearTimeout;

    public ClearTask(Key key, DuplicateFrameChecker checker) {
        this.key = key;
        this.checker = checker;
    }

    public synchronized void start() {
        cancel();
        this.clearTimeout = setTimeout(this::clear, this.checker.getClearTimeout());
    }

    public synchronized void cancel() {
        if (this.clearTimeout != null) {
            this.clearTimeout.cancel();
            this.clearTimeout = null;
        }
    }

    private void clear() {
        this.checker.clearTaskMap.remove(key);
    }

}
