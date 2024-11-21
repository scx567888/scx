package cool.scx.io;

import cool.scx.common.util.ArrayUtils;

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
        int actualLength = Math.min(maxLength, availableLength);
        byte[] result = new byte[actualLength];
        System.arraycopy(bytes, position, result, 0, actualLength);
        position += actualLength;
        return result;
    }

    @Override
    public void read(DataConsumer dataConsumer, int maxLength) throws NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        int actualLength = Math.min(maxLength, availableLength);
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
        int actualLength = Math.min(maxLength, availableLength);
        byte[] result = new byte[actualLength];
        System.arraycopy(bytes, position, result, 0, actualLength);
        return result;
    }

    @Override
    public void peek(DataConsumer dataConsumer, int maxLength) throws NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        int actualLength = Math.min(maxLength, availableLength);
        dataConsumer.accept(bytes, position, actualLength);
    }

    @Override
    public int indexOf(byte b, int max) throws NoMatchFoundException, NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        var index = ArrayUtils.indexOf(bytes, position, Math.min(bytes.length, max), b);
        if (index == -1) {
            throw new NoMatchFoundException();
        }
        return index;
    }

    @Override
    public int indexOf(byte[] b, int max) throws NoMatchFoundException, NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        var index = ArrayUtils.indexOf(bytes, position, Math.min(bytes.length, max), b);
        if (index == -1) {
            throw new NoMatchFoundException();
        }
        return index;
    }

    @Override
    public void skip(int length) throws NoMoreDataException {
        int availableLength = bytes.length - position;
        if (availableLength <= 0) {
            throw new NoMoreDataException();
        }
        this.position += length;
    }

}
