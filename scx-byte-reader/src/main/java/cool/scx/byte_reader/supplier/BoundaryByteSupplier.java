package cool.scx.byte_reader.supplier;

import cool.scx.byte_reader.ByteNode;
import cool.scx.byte_reader.IByteReader;
import cool.scx.byte_reader.exception.ByteSupplierException;
import cool.scx.byte_reader.exception.NoMatchFoundException;
import cool.scx.byte_reader.exception.NoMoreDataException;
import cool.scx.byte_reader.indexer.KMPByteIndexer;

import java.util.Arrays;

public class BoundaryByteSupplier implements ByteSupplier {

    private final IByteReader dataReader;
    private final KMPByteIndexer dataIndexer;
    private final int bufferLength;
    private boolean isFinish = false;

    public BoundaryByteSupplier(IByteReader dataReader, byte[] boundaryBytes) {
        this(dataReader, boundaryBytes, 8192);
    }

    public BoundaryByteSupplier(IByteReader dataReader, byte[] boundaryBytes, int bufferLength) {
        this.dataReader = dataReader;
        this.dataIndexer = new KMPByteIndexer(boundaryBytes);
        this.bufferLength = bufferLength;
    }

    @Override
    public ByteNode get() throws ByteSupplierException {
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
            return new ByteNode(read);
        } catch (NoMatchFoundException e) {
            // 没找到 说明可能还有 继续读取
            // 为了防止误读这里检查 patternIndex
            // 没有匹配任何 可以安全读
            if (dataIndexer.patternIndex() == 0) {
                var read = dataReader.read(bufferLength);
                return new ByteNode(read);
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
                    return new ByteNode(peek);
                }
            }
        } catch (NoMoreDataException e) {
            // 如果底层 ByteReader 没数据了，也返回 null
            return null;
        }
    }

}
