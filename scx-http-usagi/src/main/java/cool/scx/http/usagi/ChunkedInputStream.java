package cool.scx.http.usagi;

import cool.scx.http.exception.BadRequestException;
import cool.scx.io.LinkedDataReader;
import cool.scx.io.NoMoreDataException;

import java.io.InputStream;

public class ChunkedInputStream extends InputStream {

    private final LinkedDataReader chunkedDataReader;

    public ChunkedInputStream(LinkedDataReader dataReader) {
        this.chunkedDataReader = new LinkedDataReader(() -> {
            var bytes = dataReader.readUntil("\r\n".getBytes());
            var s = new String(bytes);
            int chunkLength = Integer.parseUnsignedInt(s, 16);
            //读取到结尾了
            if (chunkLength == 0) {
                var endBytes = dataReader.readUntil("\r\n".getBytes());
                var end = new String(endBytes);
                if (!end.isEmpty()) {
                    throw new BadRequestException("Invalid terminating chunk");
                }
                return null;
            }
            var nextChunkData = dataReader.read(chunkLength);
            dataReader.skip(2); // skip \r\n after the chunk
            return new LinkedDataReader.Node(nextChunkData);
        });
    }

    @Override
    public int read() {
        try {
            return chunkedDataReader.read() & 0xFF;
        } catch (NoMoreDataException e) {
            return -1;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) {
        try {
            var s = chunkedDataReader.read(len);
            System.arraycopy(s, 0, b, off, s.length);
            return s.length;
        } catch (NoMoreDataException e) {
            return -1;
        }
    }

}
