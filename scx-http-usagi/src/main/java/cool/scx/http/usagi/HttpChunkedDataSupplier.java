package cool.scx.http.usagi;

import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.data_supplier.DataSupplier;

/**
 * todo 待完成
 *
 * @author scx567888
 * @version 0.0.1
 */
public class HttpChunkedDataSupplier implements DataSupplier {

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
