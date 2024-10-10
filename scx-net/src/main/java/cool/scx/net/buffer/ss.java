package cool.scx.net.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ss {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Files.newInputStream(Path.of("C:\\Users\\scx\\Desktop\\新建文本文档 (2).txt"));
        DataReader d = new DataReader(() -> {
            try {
                return inputStream.readNBytes(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        for (int i = 0; i < 10; ) {
//            var bytes = d.findNewLine(Integer.MAX_VALUE);
//            var s = d.readAsciiString(bytes);
            var bytes1 = d.find("123".getBytes(), Integer.MAX_VALUE);
            var bytes12 = d.readBytes(bytes1);
            d.skip("123".getBytes().length);
            System.out.println(new String(bytes12));
        }

    }
}
