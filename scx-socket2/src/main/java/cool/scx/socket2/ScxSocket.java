package cool.scx.socket2;

import java.util.function.Consumer;

public interface ScxSocket {
    
    void send(byte[] data);
    
    void onMessage(Consumer<byte[]> onMessage);
    
    void onClose();
    
    void close();
    
}
