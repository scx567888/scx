package cool.scx.util.zip.zip_data_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class NullZipDataSource implements ZipDataSource {

    public NullZipDataSource() {

    }

    @Override
    public InputStream toInputStream() throws IOException {
        return InputStream.nullInputStream();
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {

    }

}
