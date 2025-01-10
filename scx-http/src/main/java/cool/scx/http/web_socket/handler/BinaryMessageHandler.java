package cool.scx.http.web_socket.handler;

public interface BinaryMessageHandler {

    void handle(byte[] binary, boolean last);

}
