package cool.scx.http.web_socket.handler;

public interface CloseHandler {

    void handle(int code, String reason);

}
