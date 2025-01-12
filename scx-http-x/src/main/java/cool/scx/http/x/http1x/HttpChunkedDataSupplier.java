package cool.scx.http.x.http1x;

import cool.scx.http.HttpStatusCode;
import cool.scx.http.exception.ScxHttpException;
import cool.scx.io.DataNode;
import cool.scx.io.DataReader;
import cool.scx.io.DataSupplier;

/**
 * 用来解析 HttpChunked 分块传输数据
 *
 * @author scx567888
 * @version 0.0.1
 */
public class HttpChunkedDataSupplier implements DataSupplier {

    private final DataReader dataReader;
    private final long maxLength;
    private final Runnable onFinish;
    private long position;
    private boolean isFinished;

    public HttpChunkedDataSupplier(DataReader dataReader) {
        this(dataReader, Long.MAX_VALUE);
    }

    public HttpChunkedDataSupplier(DataReader dataReader, long maxLength) {
        this(dataReader, maxLength, () -> {});
    }

    public HttpChunkedDataSupplier(DataReader dataReader, long maxLength, Runnable onFinish) {
        this.dataReader = dataReader;
        this.maxLength = maxLength;
        this.position = 0;
        this.isFinished = false;
        this.onFinish = onFinish;
    }

    @Override
    public DataNode get() {
        if (isFinished) {
            return null;
        }

        var chunkLengthBytes = dataReader.readUntil("\r\n".getBytes());
        var chunkLengthStr = new String(chunkLengthBytes);
        int chunkLength = Integer.parseUnsignedInt(chunkLengthStr, 16);

        //这里做最大长度限制检查
        checkMaxPayload(chunkLength);

        //读取到结尾了
        if (chunkLength == 0) {
            var endBytes = dataReader.readUntil("\r\n".getBytes());
            if (endBytes.length != 0) {
                throw new IllegalArgumentException("错误的终结分块");
            }
            //调用结束
            completeRead();
            return null;
        }
        var nextChunkData = dataReader.read(chunkLength);
        dataReader.skip(2); // skip \r\n after the chunk
        return new DataNode(nextChunkData);
    }

    public void checkMaxPayload(int chunkLength) {
        // 检查数据块大小是否超过最大值
        if (position + chunkLength > maxLength) {
            throw new ScxHttpException(HttpStatusCode.CONTENT_TOO_LARGE);
        }
        position += chunkLength;
    }

    private void completeRead() {
        if (!isFinished) {
            isFinished = true;
            onFinish.run();
        }
    }

}
