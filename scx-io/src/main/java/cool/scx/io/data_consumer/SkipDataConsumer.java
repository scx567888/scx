package cool.scx.io.data_consumer;

public class SkipDataConsumer implements DataConsumer {

    public static final SkipDataConsumer SKIP_DATA_CONSUMER = new SkipDataConsumer();

    //这个类不应该被用户实例化
    private SkipDataConsumer() {

    }

    @Override
    public boolean accept(byte[] bytes, int position, int length) {
        //什么都不做
        return true;
    }

}
