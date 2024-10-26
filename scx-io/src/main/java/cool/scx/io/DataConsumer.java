package cool.scx.io;

public interface DataConsumer {
    
    void accept(byte[] bytes, int position, int length);
    
}
