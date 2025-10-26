package cool.scx.http.media.multi_part;

import cool.scx.io.ByteChunk;
import cool.scx.io.ByteInput;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;
import cool.scx.io.indexer.KMPByteIndexer;
import cool.scx.io.supplier.ByteSupplier;

import java.io.IOException;
import java.util.Arrays;

/// BoundaryByteSupplier
///
/// @author scx567888
/// @version 0.0.1
public final class BoundaryByteSupplier implements ByteSupplier {

    private final ByteInput dataReader;
    private final KMPByteIndexer dataIndexer;
    private final int bufferLength;
    private boolean isFinish = false;

    public BoundaryByteSupplier(ByteInput dataReader, byte[] boundaryBytes) {
        this(dataReader, boundaryBytes, 8192);
    }

    public BoundaryByteSupplier(ByteInput dataReader, byte[] boundaryBytes, int bufferLength) {
        this.dataReader = dataReader;
        this.dataIndexer = new KMPByteIndexer(boundaryBytes);
        this.bufferLength = bufferLength;
    }

    @Override
    public ByteChunk get() throws Exception {
        //完成了就永远返回 null
        if (isFinish) {
            return null;
        }
        try {
            // 在 bufferLength 范围内查找
            var index = dataReader.indexOf(dataIndexer, bufferLength);
            // 找到了就全部返回
            var read = dataReader.read((int) index);
            // 找到了就要标识为完成
            isFinish = true;
            return new ByteChunk(read);
        } catch (NoMatchFoundException e) {
            // 没找到 说明可能还有 继续读取
            // 为了防止误读这里检查 matchedLength,  若为 0, 表示可以安全读
            if (dataIndexer.matchedLength() == 0) {
                var read = dataReader.read(bufferLength);
                return new ByteChunk(read);
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
                    return new ByteChunk(peek);
                }
            }
        } catch (NoMoreDataException e) {
            // 如果底层 ByteReader 没数据了, 也返回 null
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        dataReader.close();
    }

}
