package cool.scx.io.data_reader;

import cool.scx.common.util.ArrayUtils;
import cool.scx.io.data_consumer.DataConsumer;
import cool.scx.io.exception.NoMatchFoundException;
import cool.scx.io.exception.NoMoreDataException;

import static java.lang.Math.min;

/// ByteArrayDataReader
///
/// @author scx567888
/// @version 0.0.1
public class ByteArrayDataReader implements DataReader {

    private final byte[] bytes;
    private int position;

    public ByteArrayDataReader(byte[] bytes) {
        this.bytes = bytes;
        this.position = 0;
    }

    @Override
    public byte read() throws NoMoreDataException {
        try {
            var b = bytes[position];
            position = position + 1;
            return b;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoMoreDataException();
        }
    }

    @Override
    public byte[] read(int maxLength) throws NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        int actualLength = min(maxLength, availableLength);
        byte[] result = new byte[actualLength];
        System.arraycopy(bytes, position, result, 0, actualLength);
        position += actualLength;
        return result;
    }

    @Override
    public void read(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        int actualLength = (int) min(maxLength, availableLength);
        dataConsumer.accept(bytes, position, actualLength);
        position += actualLength;
    }

    @Override
    public byte peek() throws NoMoreDataException {
        try {
            return bytes[position];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoMoreDataException();
        }
    }

    @Override
    public byte[] peek(int maxLength) throws NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        int actualLength = min(maxLength, availableLength);
        byte[] result = new byte[actualLength];
        System.arraycopy(bytes, position, result, 0, actualLength);
        return result;
    }

    @Override
    public void peek(DataConsumer dataConsumer, long maxLength) throws NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        int actualLength = (int) min(maxLength, availableLength);
        dataConsumer.accept(bytes, position, actualLength);
    }

    @Override
    public long indexOf(byte b, long maxLength) throws NoMatchFoundException, NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        var index = ArrayUtils.indexOf(bytes, position, (int) min(bytes.length, maxLength), b);
        if (index == -1) {
            throw new NoMatchFoundException();
        }
        return index;
    }

    @Override
    public long indexOf(byte[] b, long maxLength) throws NoMatchFoundException, NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        var index = ArrayUtils.indexOf(bytes, position, (int) min(bytes.length, maxLength), b);
        if (index == -1) {
            throw new NoMatchFoundException();
        }
        return index;
    }

    @Override
    public void skip(long length) throws NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        if (length >= availableLength) {
            this.position = bytes.length;
        } else {
            this.position += (int) length;
        }
    }

}
