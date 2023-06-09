package cool.scx.util.zip.zip_data_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class InputStreamZipDataSource implements ZipDataSource {

    private final InputStream inputStream;

    public InputStreamZipDataSource(InputStream inputStream) {
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
