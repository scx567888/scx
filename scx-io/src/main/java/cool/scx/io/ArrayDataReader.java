package cool.scx.io;

import cool.scx.common.util.ArrayUtils;

public class ArrayDataReader implements DataReader {

    private final byte[] bytes;
    private int position;

    public ArrayDataReader(byte[] bytes) {
        this.bytes = bytes;
        this.position = 0;
    }

    @Override
    public byte read() throws NoMoreDataException {
        try {
            return bytes[position++];
        } catch (Exception e) {
            throw new NoMoreDataException();
        }
    }

    @Override
    public byte[] read(int maxLength) throws NoMoreDataException {
        return new byte[0];
    }

    @Override
    public byte get() throws NoMoreDataException {
        try {
            return bytes[position];
        } catch (Exception e) {
            throw new NoMoreDataException();
        }
    }

    @Override
    public byte[] get(int length) throws NoMoreDataException {
        return new byte[0];
    }

    @Override
    public int find(byte b) {
        return ArrayUtils.indexOf(bytes, b);
    }

    @Override
    public int find(byte[] b) {
        return ArrayUtils.indexOf(bytes, b);
    }

    @Override
    public void skip(int length) {
        this.position += length;
    }
    
}
