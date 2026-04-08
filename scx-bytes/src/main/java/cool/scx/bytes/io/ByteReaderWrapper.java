package cool.scx.bytes.io;

import cool.scx.bytes.ByteReader;
import cool.scx.bytes.IByteReader;
import cool.scx.bytes.supplier.InputStreamByteSupplier;

import java.io.InputStream;

public interface ByteReaderWrapper {

    static ByteReaderInputStream byteReaderToInputStream(IByteReader byteReader) {
        return new ByteReaderInputStream(byteReader);
    }

    static IByteReader inputStreamToByteReader(InputStream inputStream) {
        if (inputStream instanceof ByteReaderWrapper wrapper) {
            return wrapper.byteReader();
        }
        return new ByteReader(new InputStreamByteSupplier(inputStream));
    }

    IByteReader byteReader();

}
