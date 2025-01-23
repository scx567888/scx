package cool.scx.socket2;

import cool.scx.http.web_socket.ScxServerWebSocket;

public class ScxServerSocket {

    private final ScxServerWebSocket webSocket;
    private final String clientID;

    public ScxServerSocket(ScxServerWebSocket webSocket, String clientID) {
        this.webSocket = webSocket;
        this.clientID = clientID;
    }
    
    public String clientID() {
        return clientID;
    }
    
}
