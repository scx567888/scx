package cool.scx.http.usagi;

import cool.scx.io.DataNode;
import cool.scx.io.DataReader;

import java.util.function.Supplier;

public class HttpChunkedDataSupplier implements Supplier<DataNode> {

    private final DataReader dataReader;

    public HttpChunkedDataSupplier(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    @Override
    public DataNode get() {
        var chunkLengthBytes = dataReader.readUntil("\r\n".getBytes());
        var chunkLengthStr = new String(chunkLengthBytes);
        int chunkLength = Integer.parseUnsignedInt(chunkLengthStr, 16);
        //读取到结尾了
        if (chunkLength == 0) {
            var endBytes = dataReader.readUntil("\r\n".getBytes());
            if (endBytes.length != 0) {
                throw new IllegalArgumentException("错误的终结分块");
            }
            return null;
        }
        var nextChunkData = dataReader.read(chunkLength);
        dataReader.skip(2); // skip \r\n after the chunk
        return new DataNode(nextChunkData);
    }

}
