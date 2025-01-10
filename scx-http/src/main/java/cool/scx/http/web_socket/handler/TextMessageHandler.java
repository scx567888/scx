package cool.scx.http.web_socket.handler;

public interface TextMessageHandler {

    void handle(String text, boolean last);

}
