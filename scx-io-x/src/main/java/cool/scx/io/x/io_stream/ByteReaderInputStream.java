package cool.scx.io.x.io_stream;

import cool.scx.io.ByteInput;
import cool.scx.io.ByteInputMark;
import cool.scx.io.DefaultByteInput;
import cool.scx.io.consumer.FillByteArrayByteConsumer;
import cool.scx.io.consumer.OutputStreamByteConsumer;
import cool.scx.io.exception.NoMoreDataException;
import cool.scx.io.exception.ScxIOException;
import cool.scx.io.supplier.ByteSupplier;

import java.io.IOException;
import java.io.OutputStream;

/// 包装 ByteReader 的输入流
///
/// @author scx567888
/// @version 0.0.1
public class ByteReaderInputStream extends CheckedInputStream {

    private final ByteInput dataReader;
    private ByteInputMark mark;

    public ByteReaderInputStream(ByteInput dataReader) {
        this.dataReader = dataReader;
    }

    public ByteReaderInputStream(ByteSupplier dataSupplier) {
        this.dataReader = new DefaultByteInput(dataSupplier);
    }

    @Override
    public int read() throws IOException {
        ensureOpen();
        try {
            return this.dataReader.read() & 255;
        } catch (ScxIOException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException ioException) {
                throw ioException;
            } else {
                throw e;
            }
        } catch (NoMoreDataException var5) {
            return -1;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        var consumer = new FillByteArrayByteConsumer(b, off, len);

        try {
            this.dataReader.read(consumer, (long) len);
        } catch (ScxIOException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException ioException) {
                throw ioException;
            }

            throw e;
        } catch (NoMoreDataException var9) {
            return -1;
        }

        return consumer.bytesFilled();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        ensureOpen();
        var consumer = new OutputStreamByteConsumer(out);

        try {
            this.dataReader.readAll(consumer);
        } catch (ScxIOException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException ioException) {
                throw ioException;
            }

            throw e;
        }

        return consumer.bytesWritten();
    }

    public ByteInput dataReader() {
        return dataReader;
    }

    @Override
    public void mark(int readlimit) {
        this.mark = dataReader.mark();
    }

    @Override
    public void reset() throws IOException {
        if (this.mark != null) {
            this.mark.reset();
        }
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        try {
            return this.dataReader.readUpTo(len);
        } catch (ScxIOException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException ioException) {
                throw ioException;
            } else {
                throw e;
            }
        } catch (NoMoreDataException var6) {
            return new byte[0];
        }
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        try {
            return this.dataReader.readAll();
        } catch (ScxIOException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException ioException) {
                throw ioException;
            } else {
                throw e;
            }
        }
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

}
