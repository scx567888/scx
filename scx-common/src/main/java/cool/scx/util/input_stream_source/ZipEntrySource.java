package cool.scx.util.input_stream_source;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

final class ZipEntrySource implements InputStreamSource {

    private final ZipEntry zipEntry;

    private final ZipFile zipFile;

    public ZipEntrySource(ZipEntry zipEntry, ZipFile zipFile) {
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
