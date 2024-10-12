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
            return bytes[position++];
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
    public byte get() throws NoMoreDataException {
        try {
            return bytes[position];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoMoreDataException();
        }
    }

    @Override
    public byte[] get(int maxLength) throws NoMoreDataException {
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
    public int indexOf(byte b) throws NoMatchFoundException {
        var index = ArrayUtils.indexOf(bytes, position, bytes.length, b);
        if (index == -1) {
            throw new NoMatchFoundException();
        }
        return index;
    }

    @Override
    public int indexOf(byte[] b) throws NoMatchFoundException {
        var index = ArrayUtils.indexOf(bytes, position, bytes.length, b);
        if (index == -1) {
            throw new NoMatchFoundException();
        }
        return index;
    }

    @Override
    public void skip(int length) {
        this.position += length;
    }

}
