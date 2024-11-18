package cool.scx.socket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;

final class FrameSender {

    final ConcurrentMap<Long, SendTask> sendTaskMap;
    final ScheduledExecutorService executor;

    public FrameSender(ScxSocketOptions options) {
        this.sendTaskMap = new ConcurrentHashMap<>();
        this.executor = options.executor();
    }

    public void clearSendTask(ScxSocketFrame ackFrame) {
        var sendTask = this.sendTaskMap.get(ackFrame.ack_id);
        if (sendTask != null) {
            sendTask.clear();
        }
    }

    public void send(ScxSocketFrame socketFrame, SendOptions options, ScxSocket scxSocket) {
        var sendTask = new SendTask(socketFrame, options, this);
        this.sendTaskMap.put(socketFrame.seq_id, sendTask);
        sendTask.start(scxSocket);
    }

    public void startAllSendTask(ScxSocket scxSocket) {
        for (var value : this.sendTaskMap.values()) {
            value.start(scxSocket);
        }
    }

    public void cancelAllResendTask() {
        for (var value : this.sendTaskMap.values()) {
            value.cancelResend();
        }
    }

}
