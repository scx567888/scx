package cool.scx.util.input_stream_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class NullSource implements InputStreamSource {

    public NullSource() {

    }

    @Override
    public InputStream toInputStream() throws IOException {
        return InputStream.nullInputStream();
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {

    }

}
