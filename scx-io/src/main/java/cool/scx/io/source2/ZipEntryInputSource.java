package cool.scx.io.source2;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ZipEntryInputSource extends InputStreamInputSource {

    private final ZipEntry zipEntry;

    private final ZipFile zipFile;

    public ZipEntryInputSource(ZipEntry zipEntry, ZipFile zipFile) throws IOException {
        super(zipFile.getInputStream(zipEntry));
        this.zipEntry = zipEntry;
        this.zipFile = zipFile;
    }

}
