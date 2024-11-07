package cool.scx.io.source;

import cool.scx.io.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ZipEntrySource implements InputSource {

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

}
