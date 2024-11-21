package cool.scx.http.usagi;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NoCloseOutputStream extends FilterOutputStream {

    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field {@code this.out} for later use, or
     *            {@code null} if this instance is to be
     *            created without an underlying stream.
     */
    public NoCloseOutputStream(OutputStream out) {
        super(out);
    }

    @Override
    public void close() throws IOException {
        //这里什么也不做
    }
    
}
