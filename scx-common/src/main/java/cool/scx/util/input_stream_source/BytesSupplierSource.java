package cool.scx.util.input_stream_source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

final class BytesSupplierSource implements InputStreamSource {

    private final Supplier<byte[]> bytesSupplier;

    public BytesSupplierSource(Supplier<byte[]> bytesSupplier) {
        this.bytesSupplier = bytesSupplier;
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
