package cool.scx.websocket.x;

/// WebSocket 消息 太大异常
class CloseWebSocketException extends RuntimeException {

    private final int closeCode;

    public CloseWebSocketException(int closeCode, String message) {
        super(message);
        this.closeCode = closeCode;
    }

    public int closeCode() {
        return closeCode;
    }

}
