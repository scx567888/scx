package cool.scx.byte_reader.consumer;

/// DataConsumer
///
/// @author scx567888
/// @version 0.0.1
public interface ByteConsumer {

    /// @param bytes    字节
    /// @param position 起始位置
    /// @param length   长度
    /// @return 是否需要更多数据
    boolean accept(byte[] bytes, int position, int length);

}
