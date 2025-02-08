package cool.scx.common.bit_array;

public interface IBitArray {

    void set(long index, boolean value);

    boolean get(long index);

    long length();

    boolean[] toBooleans();

    byte[] toBytes();

    long[] toLongs();

    String toBinaryStrings();

}
