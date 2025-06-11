package cool.scx.byte_reader.consumer;

import cool.scx.byte_reader.ByteChunk;

/// 直接跳过
///
/// @author scx567888
/// @version 0.0.1
public class SkipDataConsumer implements ByteConsumer {

    public static final SkipDataConsumer SKIP_DATA_CONSUMER = new SkipDataConsumer();

    //这个类不应该被用户实例化
    private SkipDataConsumer() {

    }

    @Override
    public boolean accept(ByteChunk chunk) {
        //什么都不做
        return true;
    }

}
