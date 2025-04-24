package cool.scx.websocket.exception;

/// WebSocket 异常
public class WebSocketException extends RuntimeException {

    private final int closeCode;

    public WebSocketException(int closeCode, String message) {
        super(message);
        this.closeCode = closeCode;
    }

    public int closeCode() {
        return closeCode;
    }

}
