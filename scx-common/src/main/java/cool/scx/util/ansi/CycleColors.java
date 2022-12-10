package cool.scx.util.ansi;

/**
 * 循环颜色
 *
 * @author scx567888
 * @version 0.0.1
 */
final class CycleColors {

    /**
     * 颜色列表 (是一个单向循环链表)
     */
    private static Node<AnsiColor> currentColor = initColorNode();

    /**
     * 创建单向循环链表
     *
     * @return a
     */
    private static Node<AnsiColor> initColorNode() {
        var temp = new Node<AnsiColor>();
        var first = temp;
        var allColors = AnsiColor.values();
        var maxLength = allColors.length;
        var lastIndex = maxLength - 1;
        for (int i = 0; i < maxLength; i = i + 1) {
            temp.item = allColors[i];
            temp.next = i < lastIndex ? new Node<>() : first;
            temp = temp.next;
        }
        return first;
    }

    /**
     * 获取颜色
     *
     * @return 颜色
     */
    public static AnsiColor next() {
        var c = currentColor.item;
        currentColor = currentColor.next;
        return c;
    }

    private static class Node<E> {
        private E item;
        private Node<E> next;
    }

}
