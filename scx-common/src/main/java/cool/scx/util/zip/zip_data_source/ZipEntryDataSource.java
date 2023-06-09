package cool.scx.util.zip.zip_data_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

final class ZipEntryDataSource implements ZipDataSource {

    private final ZipEntry zipEntry;

    private final ZipFile zipFile;

    public ZipEntryDataSource(ZipEntry zipEntry, ZipFile zipFile) {
        this.zipEntry = zipEntry;
        this.zipFile = zipFile;
    }

    @Override
    public InputStream toInputStream() throws IOException {
        return zipFile.getInputStream(zipEntry);
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        zipFile.getInputStream(zipEntry).transferTo(out);
    }

}
