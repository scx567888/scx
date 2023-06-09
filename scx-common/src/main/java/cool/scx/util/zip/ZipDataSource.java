package cool.scx.util.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 可以将其想象成一个 BytesSupplier ,主要用来规范多种类型的数据来源 如文件 字节数组等
 *
 * @author scx567888
 * @version 2.0.4
 */
public interface ZipDataSource {

    void writeToOutputStream(OutputStream out) throws IOException;

    InputStream toInputStream() throws IOException;

}
