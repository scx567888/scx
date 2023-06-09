package cool.scx.util.zip.zip_data_source;

import cool.scx.util.zip.ZipDataSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

public final class BytesSupplierZipDataSource implements ZipDataSource {

    private final Supplier<byte[]> bytesSupplier;

    public BytesSupplierZipDataSource(Supplier<byte[]> bytesSupplier) {
        this.bytesSupplier = bytesSupplier;
    }

    public Supplier<byte[]> getBytesSupplier() {
        return bytesSupplier;
    }

    @Override
    public InputStream toInputStream() throws IOException {
        return new ByteArrayInputStream(bytesSupplier.get());
    }

    @Override
    public void writeToOutputStream(OutputStream out) throws IOException {
        out.write(bytesSupplier.get());
    }

}
