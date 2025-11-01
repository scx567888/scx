package cool.scx.http.x.http1;

import cool.scx.io.ByteInput;
import cool.scx.io.ByteInputMark;
import cool.scx.io.ByteMatchResult;
import cool.scx.io.ByteOutput;
import cool.scx.io.consumer.ByteConsumer;
import cool.scx.io.exception.AlreadyClosedException;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;
import cool.scx.io.exception.ScxIOException;
import cool.scx.io.indexer.ByteIndexer;

import java.io.OutputStream;

// todo 待实现
public class AutoContinueByteInput implements ByteInput {

    private final ByteInput in;
    private final ByteOutput out;
    private boolean continueSent;

    public AutoContinueByteInput(ByteInput in, ByteOutput out) {
        this.in = in;
        this.out = out;
        this.continueSent = false;
    }

    @Override
    public byte read() throws ScxIOException, AlreadyClosedException, NoMoreDataException {
        return 0;
    }

    @Override
    public <X extends Throwable> void read(ByteConsumer<X> byteConsumer, long maxLength) throws X, ScxIOException, AlreadyClosedException, NoMoreDataException {

    }

    @Override
    public <X extends Throwable> void readUpTo(ByteConsumer<X> byteConsumer, long length) throws X, ScxIOException, AlreadyClosedException, NoMoreDataException {

    }

    @Override
    public <X extends Throwable> void readFully(ByteConsumer<X> byteConsumer, long length) throws X, ScxIOException, AlreadyClosedException, NoMoreDataException {

    }

    @Override
    public byte peek() throws ScxIOException, AlreadyClosedException, NoMoreDataException {
        return 0;
    }

    @Override
    public <X extends Throwable> void peek(ByteConsumer<X> byteConsumer, long maxLength) throws X, ScxIOException, AlreadyClosedException, NoMoreDataException {

    }

    @Override
    public <X extends Throwable> void peekUpTo(ByteConsumer<X> byteConsumer, long length) throws X, ScxIOException, AlreadyClosedException, NoMoreDataException {

    }

    @Override
    public <X extends Throwable> void peekFully(ByteConsumer<X> byteConsumer, long length) throws X, ScxIOException, AlreadyClosedException, NoMoreDataException {

    }

    @Override
    public ByteMatchResult indexOf(ByteIndexer indexer, long maxLength) throws NoMatchFoundException, ScxIOException, AlreadyClosedException, NoMoreDataException {
        return null;
    }

    @Override
    public ByteInputMark mark() throws AlreadyClosedException {
        return null;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void close() throws ScxIOException {

    }

}
