package cool.scx.io;

public class SkipDataPuller implements DataPuller {

    public static final SkipDataPuller SKIP_DATA_PULLER = new SkipDataPuller();

    //这个类不应该被用户实例化
    private SkipDataPuller() {

    }

    @Override
    public PullResult pull() {
        return PullResult.BREAK;
    }

}
