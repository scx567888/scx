package cool.scx.http.x.http1.chunked;

import cool.scx.byte_reader.ByteChunk;
import cool.scx.byte_reader.ByteNode;
import cool.scx.byte_reader.ByteReader;
import cool.scx.byte_reader.exception.ByteSupplierException;
import cool.scx.byte_reader.exception.NoMatchFoundException;
import cool.scx.byte_reader.supplier.ByteSupplier;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.exception.ContentTooLargeException;

/// 用来解析 HttpChunked 分块传输数据
///
/// @author scx567888
/// @version 0.0.1
public class HttpChunkedDataSupplier implements ByteSupplier {

    private final ByteReader dataReader;
    private final long maxLength;
    private long position;
    private boolean isFinished;

    public HttpChunkedDataSupplier(ByteReader dataReader) {
        this(dataReader, Long.MAX_VALUE);
    }

    public HttpChunkedDataSupplier(ByteReader dataReader, long maxLength) {
        this.dataReader = dataReader;
        this.maxLength = maxLength;
        this.position = 0;
        this.isFinished = false;
    }

    @Override
    public ByteChunk get() throws ByteSupplierException {
        if (isFinished) {
            return null;
        }

        var chunkLengthBytes = dataReader.readUntil("\r\n".getBytes());
        var chunkLengthStr = new String(chunkLengthBytes);
        int chunkLength;
        try {
            chunkLength = Integer.parseUnsignedInt(chunkLengthStr, 16);
        } catch (NumberFormatException e) {
            throw new BadRequestException("错误的分块长度 !!!" + chunkLengthStr);
        }

        //这里做最大长度限制检查
        checkMaxPayload(chunkLength);

        //读取到结尾了
        if (chunkLength == 0) {
            byte[] endBytes;
            try {
                endBytes = dataReader.readUntil("\r\n".getBytes());
            } catch (NoMatchFoundException e) {
                throw new BadRequestException("错误的终结分块, 终结块不完整：缺少 \\r\\n !!!");
            }

            if (endBytes.length != 0) {
                throw new BadRequestException("错误的终结分块, 应为空块但发现了内容 !!!");
            }
            isFinished = true;
            return null;
        }
        var nextChunkData = dataReader.read(chunkLength);
        dataReader.skip(2); // skip \r\n after the chunk
        return new ByteChunk(nextChunkData);
    }

    public void checkMaxPayload(int chunkLength) {
        // 检查数据块大小是否超过最大值
        if (position + chunkLength > maxLength) {
            throw new ContentTooLargeException();
        }
        position += chunkLength;
    }

}
