package cool.scx.http.x.http1.chunked;

import cool.scx.io.ByteChunk;
import cool.scx.io.ByteInput;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.supplier.ByteSupplier;
import cool.scx.http.exception.BadRequestException;
import cool.scx.http.exception.ContentTooLargeException;

/// 用来解析 HttpChunked 分块传输数据
///
/// @author scx567888
/// @version 0.0.1
public class HttpChunkedByteSupplier implements ByteSupplier {

    private final ByteInput byteInput;
    private final long maxLength;
    private long position;
    private boolean isFinished;

    public HttpChunkedByteSupplier(ByteInput byteInput) {
        this(byteInput, Long.MAX_VALUE);
    }

    public HttpChunkedByteSupplier(ByteInput byteInput, long maxLength) {
        this.byteInput = byteInput;
        this.maxLength = maxLength;
        this.position = 0;
        this.isFinished = false;
    }

    @Override
    public ByteChunk get() throws ScxIOException {
        if (isFinished) {
            return null;
        }

        var chunkLengthBytes = byteInput.readUntil("\r\n".getBytes());
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
                endBytes = byteInput.readUntil("\r\n".getBytes());
            } catch (NoMatchFoundException e) {
                throw new BadRequestException("错误的终结分块, 终结块不完整: 缺少 \\r\\n !!!");
            }

            if (endBytes.length != 0) {
                throw new BadRequestException("错误的终结分块, 应为空块但发现了内容 !!!");
            }
            isFinished = true;
            return null;
        }
        var nextChunkData = byteInput.read(chunkLength);
        byteInput.skip(2); // skip \r\n after the chunk
        return ByteChunk.of(nextChunkData);
    }

    public void checkMaxPayload(int chunkLength) {
        // 检查数据块大小是否超过最大值
        if (position + chunkLength > maxLength) {
            throw new ContentTooLargeException();
        }
        position += chunkLength;
    }

    @Override
    public void close() throws ScxIOException, AlreadyClosedException {
        byteInput.close();
    }

}
