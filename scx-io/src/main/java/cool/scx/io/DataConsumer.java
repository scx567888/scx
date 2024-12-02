package cool.scx.io;

/**
 * DataConsumer
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface DataConsumer {

    void accept(byte[] bytes, int position, int length);

}
