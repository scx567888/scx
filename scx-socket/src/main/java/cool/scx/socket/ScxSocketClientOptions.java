package cool.scx.socket;

import java.util.concurrent.ScheduledExecutorService;

public final class ScxSocketClientOptions extends PingPongOptions {

    private int reconnectTimeout;

    public ScxSocketClientOptions() {
        this.reconnectTimeout = 1000 * 5;
    }

    public int getReconnectTimeout() {
        return reconnectTimeout;
    }

    public ScxSocketClientOptions setReconnectTimeout(int reconnectTimeout) {
        this.reconnectTimeout = reconnectTimeout;
        return this;
    }

    @Override
    public ScxSocketClientOptions executor(ScheduledExecutorService executor) {
        super.executor(executor);
        return this;
    }

    @Override
    public ScxSocketClientOptions setDuplicateFrameCheckerClearTimeout(int duplicateFrameCheckerClearTimeout) {
        super.setDuplicateFrameCheckerClearTimeout(duplicateFrameCheckerClearTimeout);
        return this;
    }

}
