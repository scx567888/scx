package cool.scx.io.data_consumer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

public class OutputStreamDataConsumer implements DataConsumer {

    private final OutputStream out;

    public OutputStreamDataConsumer(OutputStream out) {
        this.out = out;
    }

    @Override
    public void accept(byte[] bytes, int position, int length) {
        try {
            out.write(bytes, position, length);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
