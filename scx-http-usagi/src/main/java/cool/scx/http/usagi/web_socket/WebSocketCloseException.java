package cool.scx.http.usagi.web_socket;

// WebSocket 消息 太大异常
public class WebSocketCloseException extends RuntimeException {

    private final int closeCode;

    public WebSocketCloseException(int closeCode, String message) {
        super(message);
        this.closeCode = closeCode;
    }

    public int closeCode() {
        return closeCode;
    }

}
