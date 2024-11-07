package cool.scx.io.io_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamSource implements InputSource {

    private final InputStream inputStream;

    public InputStreamSource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public InputStream toInputStream() {
        return inputStream;
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        inputStream.transferTo(out);
    }

    @Override
    public byte[] toBytes() throws IOException {
        return inputStream.readAllBytes();
    }

}
