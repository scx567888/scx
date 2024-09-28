package cool.scx.http.media.path;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathHelper {

    public static long getFileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fileCopy(Path path, OutputStream out) {
        try (var inputStream = Files.newInputStream(path); out) {
            inputStream.transferTo(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fileCopyWithOffset(Path path, OutputStream out, long offset, long length) {
        try (var channel = Files.newByteChannel(path); var outChannel = Channels.newChannel(out)) {
            channel.position(offset);
            long toRead = length;
            var buffer = ByteBuffer.allocate(8192);
            while (toRead > 0) {
                int read = channel.read(buffer);
                if (read == -1) { // 处理文件末尾的情况
                    break;
                }
                buffer.flip();
                int toWrite = (int) Math.min(toRead, read);
                buffer.limit(toWrite);
                outChannel.write(buffer);
                buffer.clear();
                toRead = toRead - toWrite;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }

}
