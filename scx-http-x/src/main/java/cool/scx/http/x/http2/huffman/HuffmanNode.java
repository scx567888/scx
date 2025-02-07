package cool.scx.http.x.http2.huffman;

class HuffmanNode {

    int frequency;
    byte data;
    HuffmanNode left;
    HuffmanNode right;

    HuffmanNode(int frequency, byte data) {
        this.frequency = frequency;
        this.data = data;
    }

    HuffmanNode(int frequency, byte data, HuffmanNode left, HuffmanNode right) {
        this.frequency = frequency;
        this.data = data;
        this.left = left;
        this.right = right;
    }
    
}
