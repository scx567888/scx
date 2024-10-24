package cool.scx.http.media.path;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
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
        try (var raf = new RandomAccessFile(path.toFile(), "r"); out) {
            //先移动文件指针
            raf.seek(offset);
            //循环发送
            var buffer = new byte[8192];
            while (length > 0) {
                int i = raf.read(buffer, 0, (int) Math.min(buffer.length, length));
                if (i == -1) {
                    break; // 处理文件结束情况
                }
                out.write(buffer, 0, i);
                length -= i;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
