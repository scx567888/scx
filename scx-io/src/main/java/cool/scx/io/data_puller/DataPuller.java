package cool.scx.io.data_puller;

/**
 * DataPuller
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface DataPuller {

    PullResult pull();

    /**
     * 拉取结果
     */
    enum PullResult {

        /**
         * 拉取成功
         */
        SUCCESS,

        /**
         * 中断
         */
        BREAK,

        /**
         * 拉取失败
         */
        FAIL

    }

}
