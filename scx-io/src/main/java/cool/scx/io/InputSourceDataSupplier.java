package cool.scx.io;

import cool.scx.io.LinkedDataReader.Node;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

public class InputSourceDataSupplier implements Supplier<Node> {

    private final InputSource inputSource;
    private final int bufferLength;

    public InputSourceDataSupplier(InputSource inputSource, int bufferLength) {
        this.inputSource = inputSource;
        this.bufferLength = bufferLength;
    }

    public InputSourceDataSupplier(InputSource inputSource) {
        this(inputSource, 8192);
    }

    @Override
    public Node get() {
        try {
            var bytes = inputSource.read(bufferLength);
            return bytes != null ? new Node(bytes) : null;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
