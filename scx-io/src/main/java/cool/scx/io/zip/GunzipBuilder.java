package cool.scx.io.zip;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * 用来解压缩
 */
public class GunzipBuilder extends FilterInputStream {

    public GunzipBuilder(InputStream inputStream) throws IOException {
        super(new GZIPInputStream(inputStream));
    }

}
