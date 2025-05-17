package cool.scx.http.x.http1.chunked;

import cool.scx.http.exception.BadRequestException;
import cool.scx.http.exception.ContentTooLargeException;
import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_supplier.DataSupplier;
import cool.scx.io.exception.DataSupplierException;

/// 用来解析 HttpChunked 分块传输数据
///
/// @author scx567888
/// @version 0.0.1
public class HttpChunkedDataSupplier implements DataSupplier {

    private final DataReader dataReader;
    private final long maxLength;
    private long position;
    private boolean isFinished;

    public HttpChunkedDataSupplier(DataReader dataReader) {
        this(dataReader, Long.MAX_VALUE);
    }

    public HttpChunkedDataSupplier(DataReader dataReader, long maxLength) {
        this.dataReader = dataReader;
        this.maxLength = maxLength;
        this.position = 0;
        this.isFinished = false;
    }

    @Override
    public DataNode get() throws DataSupplierException {
        if (isFinished) {
            return null;
        }

        var chunkLengthBytes = dataReader.readUntil("\r\n".getBytes());
        var chunkLengthStr = new String(chunkLengthBytes);
        int chunkLength;
        try {
            chunkLength = Integer.parseUnsignedInt(chunkLengthStr, 16);
        } catch (NumberFormatException e) {
            throw new BadRequestException("错误的分块长度 !!! " + chunkLengthStr);
        }

        //这里做最大长度限制检查
        checkMaxPayload(chunkLength);

        //读取到结尾了
        if (chunkLength == 0) {
            var endBytes = dataReader.readUntil("\r\n".getBytes());
            if (endBytes.length != 0) {
                throw new BadRequestException("错误的终结分块 !!!");
            }
            isFinished = true;
            return null;
        }
        var nextChunkData = dataReader.read(chunkLength);
        dataReader.skip(2); // skip \r\n after the chunk
        return new DataNode(nextChunkData);
    }

    public void checkMaxPayload(int chunkLength) {
        // 检查数据块大小是否超过最大值
        if (position + chunkLength > maxLength) {
            throw new ContentTooLargeException();
        }
        position += chunkLength;
    }

}
