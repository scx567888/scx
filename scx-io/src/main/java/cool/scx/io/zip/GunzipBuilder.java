package cool.scx.io.zip;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * 用来解压缩 这里直接使用 GZIPInputStream 来完成
 */
public class GunzipBuilder extends GZIPInputStream {

    public GunzipBuilder(InputStream in) throws IOException {
        super(in);
    }

    public GunzipBuilder(InputStream in, int size) throws IOException {
        super(in, size);
    }

}
