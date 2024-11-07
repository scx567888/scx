package cool.scx.io.source2;

import cool.scx.io.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class NullSource implements InputSource {

    public NullSource() {

    }

    @Override
    public byte[] read(int length) throws IOException {
        return new byte[]{};
    }

    @Override
    public byte[] readAll() throws IOException {
        return new byte[]{};
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        return 0;
    }

    @Override
    public void transferTo(Path outputPath, OpenOption... options) throws IOException {

    }

    @Override
    public InputStream toInputStream() throws IOException {
        return InputStream.nullInputStream();
    }

    @Override
    public void close() throws Exception {

    }
    
}
