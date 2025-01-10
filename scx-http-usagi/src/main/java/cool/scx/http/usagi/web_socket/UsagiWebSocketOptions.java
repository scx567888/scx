package cool.scx.http.usagi.web_socket;

public class UsagiWebSocketOptions {
    
    private boolean mergeWebSocketFrame;//是否合并 WebSocket 帧
    private int maxWebSocketFrameSize; // 最大单个 WebSocket 帧长度
    private int maxWebSocketMessageSize;// 最大 WebSocket 消息长度 (可能由多个帧合并)

    public UsagiWebSocketOptions() {
        this.mergeWebSocketFrame = true; // 默认 合并 websocket 帧
        this.maxWebSocketFrameSize = 1024 * 1024 * 16; // 默认 16 MB
        this.maxWebSocketMessageSize = 1024 * 1024 * 16; // 默认 16 MB
    }

    public UsagiWebSocketOptions(UsagiWebSocketOptions oldOptions) {
        mergeWebSocketFrame(oldOptions.mergeWebSocketFrame());
        maxWebSocketFrameSize(oldOptions.maxWebSocketFrameSize());
        maxWebSocketMessageSize(oldOptions.maxWebSocketMessageSize());
    }

    public boolean mergeWebSocketFrame() {
        return mergeWebSocketFrame;
    }

    public UsagiWebSocketOptions mergeWebSocketFrame(boolean mergeWebSocketFrame) {
        this.mergeWebSocketFrame = mergeWebSocketFrame;
        return this;
    }

    public int maxWebSocketFrameSize() {
        return maxWebSocketFrameSize;
    }

    public UsagiWebSocketOptions maxWebSocketFrameSize(int maxWebSocketFrameSize) {
        this.maxWebSocketFrameSize = maxWebSocketFrameSize;
        return this;
    }

    public int maxWebSocketMessageSize() {
        return maxWebSocketMessageSize;
    }

    public UsagiWebSocketOptions maxWebSocketMessageSize(int maxWebSocketMessageSize) {
        this.maxWebSocketMessageSize = maxWebSocketMessageSize;
        return this;
    }
    
}
