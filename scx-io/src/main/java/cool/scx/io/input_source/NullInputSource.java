package cool.scx.io.input_source;

import cool.scx.io.InputSource;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class NullInputSource implements InputSource {

    public NullInputSource() {

    }

    @Override
    public byte[] read(int length) {
        return new byte[]{};
    }

    @Override
    public byte[] readAll() {
        return new byte[]{};
    }

    @Override
    public long transferTo(OutputStream out) {
        return 0;
    }

    @Override
    public void transferTo(Path outputPath, OpenOption... options) {

    }

    @Override
    public InputStream toInputStream() {
        return InputStream.nullInputStream();
    }

    @Override
    public void close() {

    }

}
