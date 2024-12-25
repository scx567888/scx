package cool.scx.io;

import static cool.scx.io.DataPuller.PullResult.BREAK;

/**
 * 计数 DataPuller
 *
 * @author scx567888
 * @version 0.0.1
 */
public class CountingDataPuller implements DataPuller {

    private final DataPuller dataPuller;
    private final int maxCount;
    private int count;

    public CountingDataPuller(DataPuller dataPuller, int maxCount) {
        this.dataPuller = dataPuller;
        this.maxCount = maxCount;
        this.count = 0;
    }

    @Override
    public PullResult pull() {
        if (count < maxCount) {
            var b = dataPuller.pull();
            count = count + 1;
            return b;
        }
        return BREAK;
    }

}
