package cool.scx.net.test;

class Node {

    byte[] bytes;
    int position;
    int length;
    Node next;

    Node(byte[] bytes, int length) {
        this.bytes = bytes;
        this.position = 0;
        this.length = length;
        this.next = null;
    }

    boolean hasAvailable() {
        return position < length;
    }

}
