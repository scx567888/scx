package cool.scx.io.input_source;

import cool.scx.io.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class InputStreamInputSource implements InputSource {

    private final InputStream inputStream;

    public InputStreamInputSource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public byte[] read(int length) throws IOException {
        //不使用 readNBytes 防止阻塞
        var bytes = new byte[length];
        int i = inputStream.read(bytes);
        if (i == -1) {
            return null; // end of data
        }
        // 如果读取的数据量与缓冲区大小一致，直接返回内部数组
        if (i == length) {
            return bytes;
        } else {
            var data = new byte[i];
            System.arraycopy(bytes, 0, data, 0, i);
            return data;
        }
    }

    @Override
    public byte[] readAll() throws IOException {
        var data = inputStream.readAllBytes();
        return data.length > 0 ? data : null;
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return inputStream.transferTo(out);
    }

    @Override
    public void transferTo(Path outputPath, OpenOption... options) throws IOException {
        try (var outputStream = Files.newOutputStream(outputPath, options)) {
            inputStream.transferTo(outputStream);
        }
    }

    @Override
    public InputStream toInputStream() {
        return inputStream;
    }

    @Override
    public void close() throws Exception {
        inputStream.close();
    }

}
