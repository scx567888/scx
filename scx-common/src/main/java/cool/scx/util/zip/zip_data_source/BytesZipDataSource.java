package cool.scx.util.zip.zip_data_source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class BytesZipDataSource implements ZipDataSource {

    private final byte[] bytes;

    public BytesZipDataSource(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public InputStream toInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        out.write(bytes);
    }

}
