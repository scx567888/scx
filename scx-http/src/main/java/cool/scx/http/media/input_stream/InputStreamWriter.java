package cool.scx.http.media.input_stream;

import cool.scx.http.media.MediaWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/// InputStreamWriter
///
/// @author scx567888
/// @version 0.0.1
public class InputStreamWriter implements MediaWriter {

    private final InputStream inputStream;

    public InputStreamWriter(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        // 直接传输的时候 一般表示 整个输入流已经被用尽了 所以这里我们顺便关闭输入流
        try (inputStream; outputStream) {
            inputStream.transferTo(outputStream);
        }
    }

}
