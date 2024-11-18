package cool.scx.socket;

import cool.scx.scheduling.ScheduleStatus;

import java.lang.System.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static cool.scx.scheduling.ScxScheduling.setTimeout;
import static cool.scx.socket.Helper.getDelayed;
import static java.lang.Math.max;
import static java.lang.System.Logger.Level.DEBUG;

final class SendTask {

    private static final Logger logger = System.getLogger(SendTask.class.getName());

    private final ScxSocketFrame socketFrame;
    private final SendOptions options;
    private final AtomicInteger sendTimes;
    private final FrameSender sender;
    private ScheduleStatus resendTask;
    private final Lock lock = new ReentrantLock();

    public SendTask(ScxSocketFrame socketFrame, SendOptions options, FrameSender sender) {
        this.socketFrame = socketFrame;
        this.options = options;
        this.sendTimes = new AtomicInteger(0);
        this.sender = sender;
    }

    public void start(ScxSocket scxSocket) {
        lock.lock();
        try {
            //当前 websocket 不可用
            if (scxSocket.isClosed()) {
                return;
            }
            //当前已经存在一个 发送中(并未完成发送) 的任务
            //todo 处理并发问题
//        if (this.sendFuture != null && !this.sendFuture.isComplete()) {
//            return;
//        }
            //超过最大发送次数
            if (this.sendTimes.get() > options.getMaxResendTimes()) {
                if (options.getGiveUpIfReachMaxResendTimes()) {
                    this.clear();
                }
                return;
            }
            //根据不同序列化配置发送不同消息

            try {

                scxSocket.webSocket.send(this.socketFrame.toJson());

                var currentSendTime = sendTimes.getAndIncrement();
                //当需要 ack 时 创建 重复发送 延时
                if (options.getNeedAck()) {
                    //计算重新发送延时
                    var resendDelayed = max(getDelayed(currentSendTime), options.getMaxResendDelayed());
                    this.resendTask = setTimeout(() -> start(scxSocket), resendDelayed);
                } else {
                    this.clear();
                }

                //LOGGER
                if (logger.isLoggable(DEBUG)) {
                    logger.log(DEBUG, "CLIENT_ID : {0}, 发送成功 : {1}", scxSocket.clientID(), this.socketFrame.toJson());
                }

            } catch (Exception e) {

                //LOGGER
                if (logger.isLoggable(DEBUG)) {
                    logger.log(DEBUG, "CLIENT_ID : {0}, 发送失败 : {1}", scxSocket.clientID(), this.socketFrame.toJson(), e);
                }

            }
        } finally {
            lock.unlock();
        }
    }

    public void cancelResend() {
        lock.lock();
        try {
            if (this.resendTask != null) {
                this.resendTask.cancel();
                this.resendTask = null;
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从任务列表中移除此任务
     */
    public void clear() {
        cancelResend();
        this.sender.sendTaskMap.remove(socketFrame.seq_id);
    }

    public ScxSocketFrame socketFrame() {
        return this.socketFrame;
    }

}
