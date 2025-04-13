package cool.scx.websocket.handler;

public interface CloseHandler {

    void handle(int code, String reason);

}
