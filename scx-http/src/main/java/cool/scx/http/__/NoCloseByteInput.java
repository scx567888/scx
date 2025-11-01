package cool.scx.http.__;

import cool.scx.io.DefaultByteInput;
import cool.scx.io.exception.ScxIOException;
import cool.scx.io.supplier.ByteSupplier;

public class NoCloseByteInput extends DefaultByteInput {

    public NoCloseByteInput(ByteSupplier byteSupplier) {
        super(byteSupplier);
    }

    @Override
    public void close() throws ScxIOException {
        this.closed = true;
    }

}
