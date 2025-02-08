package cool.scx.common.bit_array;

public interface IBitArray {

    void set(int index, boolean value);

    boolean get(int index);

    int length();

    byte[] toBytes();

    String toBinaryString();

}
