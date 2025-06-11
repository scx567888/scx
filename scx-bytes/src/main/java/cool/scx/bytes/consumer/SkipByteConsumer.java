package cool.scx.bytes.consumer;

import cool.scx.bytes.ByteChunk;

/// 直接跳过
///
/// @author scx567888
/// @version 0.0.1
public class SkipByteConsumer implements ByteConsumer {

    public static final SkipByteConsumer SKIP_DATA_CONSUMER = new SkipByteConsumer();

    //这个类不应该被用户实例化
    private SkipByteConsumer() {

    }

    @Override
    public boolean accept(ByteChunk chunk) {
        //什么都不做
        return true;
    }

}
