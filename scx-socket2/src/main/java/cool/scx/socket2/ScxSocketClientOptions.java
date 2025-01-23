package cool.scx.socket2;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScxSocketClientOptions {

    private Executor executor;
    private ScheduledExecutorService scheduledExecutor;

    public ScxSocketClientOptions() {
        this.executor = Executors.newSingleThreadExecutor();
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public void executor(Executor executor) {
        this.executor = executor;
    }

    public void scheduledExecutor(ScheduledExecutorService scheduledExecutor) {
        this.scheduledExecutor = scheduledExecutor;
    }

    public Executor executor() {
        return executor;
    }

    public ScheduledExecutorService scheduledExecutor() {
        return scheduledExecutor;
    }
    
}
