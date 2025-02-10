package cool.scx.common.bit_array;

/**
 * BitArray 可以理解为一个 boolean[] (bit 数组) 但是兼具了一些动态长度的功能
 *
 * @author scx567888
 * @version 0.0.1
 */
public interface IBitArray extends Iterable<Boolean> {

    void set(int index, boolean value) throws IndexOutOfBoundsException;

    void set(int fromIndex, int toIndex, boolean value) throws IndexOutOfBoundsException;

    boolean get(int index) throws IndexOutOfBoundsException;

    IBitArray get(int fromIndex, int toIndex) throws IndexOutOfBoundsException;

    void length(int length);

    int length();

    byte[] toBytes();

    String toBinaryString();

    void append(boolean value);

    void append(IBitArray other);

}
