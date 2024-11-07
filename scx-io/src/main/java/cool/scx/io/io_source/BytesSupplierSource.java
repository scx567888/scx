package cool.scx.io.io_source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.function.Supplier;

public class BytesSupplierSource implements InputSource {

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

    @Override
    public byte[] toBytes() throws IOException {
        return bytesSupplier.get();
    }

    @Override
    public void toFile(Path outputPath, OpenOption... options) throws IOException {
        Files.write(outputPath, bytesSupplier.get(), options);
    }

}
