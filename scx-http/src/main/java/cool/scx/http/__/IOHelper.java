package cool.scx.http.__;

import cool.scx.io.ByteOutput;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

// todo 待移除
public final class IOHelper {

    public static long getFileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeFileToOut(Path path, ByteOutput byteOutput) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            //todo 待转换
//            inputStream.transferTo(out);
        }

    }

    public static void writeFileToOut(Path path, ByteOutput out, long offset, long length) throws IOException {
        try (var raf = new RandomAccessFile(path.toFile(), "r")) {
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
        }
    }

}
