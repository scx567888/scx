package cool.scx.http.media.byte_input;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.headers.ScxHttpHeadersWritable;
import cool.scx.http.media.MediaWriter;
import cool.scx.io.ByteInput;
import cool.scx.io.NullByteInput;
import cool.scx.io.consumer.OutputStreamByteConsumer;

import java.io.IOException;
import java.io.OutputStream;

public class ByteInputWriter implements MediaWriter {

    private final ByteInput byteInput;

    public ByteInputWriter(ByteInput byteInput) {
        this.byteInput = byteInput;
    }

    @Override
    public long beforeWrite(ScxHttpHeadersWritable responseHeaders, ScxHttpHeaders requestHeaders) {
        if (byteInput instanceof NullByteInput) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        // 直接传输的时候 一般表示 整个输入流已经被用尽了 所以这里我们顺便关闭输入流
        try (byteInput; outputStream) {
            byteInput.readAll(new OutputStreamByteConsumer(outputStream));
        }
    }

}
