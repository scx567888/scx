package cool.scx.collections.bit_array;

import java.util.Iterator;

class BitArrayIterator implements Iterator<Boolean> {

    private final IBitArray bitArray;
    private int currentIndex;

    public BitArrayIterator(IBitArray bitArray) {
        this.bitArray = bitArray;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < bitArray.length();
    }

    @Override
    public Boolean next() {
        var b = bitArray.get(currentIndex);
        currentIndex = currentIndex + 1;
        return b;
    }

}
