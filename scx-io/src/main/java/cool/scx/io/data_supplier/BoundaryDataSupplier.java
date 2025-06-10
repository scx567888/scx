package cool.scx.io.data_supplier;

import cool.scx.io.data_indexer.KMPDataIndexer;
import cool.scx.io.data_node.DataNode;
import cool.scx.io.data_reader.DataReader;
import cool.scx.io.exception.DataSupplierException;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;

import java.util.Arrays;

public class BoundaryDataSupplier implements DataSupplier {

    private final DataReader dataReader;
    private final KMPDataIndexer dataIndexer;
    private final int bufferLength;
    private boolean isFinish = false;

    public BoundaryDataSupplier(DataReader dataReader, byte[] boundaryBytes) {
        this(dataReader, boundaryBytes, 8192);
    }

    public BoundaryDataSupplier(DataReader dataReader, byte[] boundaryBytes, int bufferLength) {
        this.dataReader = dataReader;
        this.dataIndexer = new KMPDataIndexer(boundaryBytes);
        this.bufferLength = bufferLength;
    }

    @Override
    public DataNode get() throws DataSupplierException {
        //完成了就永远返回 null
        if (isFinish) {
            return null;
        }
        try {
            // 在 bufferLength 范围内查找
            var index = dataReader.indexOf(dataIndexer, bufferLength, Long.MAX_VALUE);
            // 找到了就全部返回 
            var read = dataReader.read((int) index);
            // 找到了就要标识为完成
            isFinish = true;
            return new DataNode(read);
        } catch (NoMatchFoundException e) {
            // 没找到 说明可能还有 继续读取
            // 为了防止误读这里检查 patternIndex
            // 没有匹配任何 可以安全读
            if (dataIndexer.patternIndex() == 0) {
                var read = dataReader.read(bufferLength);
                return new DataNode(read);
            } else {
                // 已经匹配到了一部分 检查是否是 真正的匹配
                var peek = dataReader.peek(dataIndexer.pattern().length);
                // 完全匹配 返回 null
                var match = Arrays.equals(peek, dataIndexer.pattern());
                if (match) {
                    isFinish = true;
                    return null;
                } else {
                    // 不匹配 继续读 这里注意 skip 索引
                    dataReader.skip(dataIndexer.pattern().length);
                    return new DataNode(peek);
                }
            }
        } catch (NoMoreDataException e) {
            // 如果底层 DataReader 没数据了，也返回 null
            return null;
        }
    }

}
