package cool.scx.io;

public class SkipDataConsumer implements DataConsumer {

    public static final SkipDataConsumer SKIP_DATA_CONSUMER = new SkipDataConsumer();

    //这个类不应该被用户实例化
    private SkipDataConsumer() {

    }

    @Override
    public void accept(byte[] bytes, int position, int length) {
        //什么都不做
    }

}
