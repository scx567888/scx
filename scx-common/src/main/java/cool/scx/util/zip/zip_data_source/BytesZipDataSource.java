package cool.scx.util.zip.zip_data_source;

import cool.scx.util.zip.ZipDataSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class BytesZipDataSource implements ZipDataSource {

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
