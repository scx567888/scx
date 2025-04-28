package cool.scx.io.data_consumer;

/// DataConsumer
///
/// @author scx567888
/// @version 0.0.1
public interface DataConsumer {

    /// @param bytes    字节
    /// @param position 起始位置
    /// @param length   长度
    /// @return 是否需要继续
    boolean accept(byte[] bytes, int position, int length);

}
