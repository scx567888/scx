package cool.scx.http.media.input_stream;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.io.ByteOutput;
import cool.scx.io.adapter.ByteOutputAdapter;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.ScxIOException;

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
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        // todo 如何判断一个流是 null
//        if (inputStream instanceof NullCheckedInputStream) {
//            return 0;
//        } else {
            return -1;
//        }
    }

    @Override
    public void write(ByteOutput byteOutput) throws ScxIOException, AlreadyClosedException {
        // 直接传输的时候 一般表示 整个输入流已经被用尽了 所以这里我们顺便关闭输入流
        try (inputStream; byteOutput) {
            inputStream.transferTo(ByteOutputAdapter.byteOutputToOutputStream(byteOutput));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
