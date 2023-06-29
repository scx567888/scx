package cool.scx.util.input_stream_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class RawInputStreamSource implements InputStreamSource {

    private final InputStream inputStream;

    public RawInputStreamSource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public InputStream toInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        inputStream.transferTo(out);
    }

}
