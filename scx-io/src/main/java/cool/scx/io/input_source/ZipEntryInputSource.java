package cool.scx.io.input_source;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ZipEntryInputSource extends LazyInputStreamInputSource {

    private final ZipEntry zipEntry;
    private final ZipFile zipFile;

    public ZipEntryInputSource(ZipEntry zipEntry, ZipFile zipFile) {
        this.zipEntry = zipEntry;
        this.zipFile = zipFile;
    }

    @Override
    public InputStream toInputStream0() throws IOException {
        return zipFile.getInputStream(zipEntry);
    }

}
