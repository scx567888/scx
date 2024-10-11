package cool.scx.io.test.t;

import java.util.function.Supplier;

public class ArrayFinder {

    public static int indexOf(Supplier<byte[]> supplier, byte[] pattern) {

        if (pattern == null || pattern.length == 0) {
            return -1;
        }

        int patternLength = pattern.length;
        int globalIndex = 0; // 跟踪整体扫描的位置
        int matched = 0; // 跟踪当前匹配的字节数

        byte[] currentArray = supplier.get();
        int arrayIndex = 0;

        while (currentArray != null) {
            while (arrayIndex < currentArray.length) {
                if (currentArray[arrayIndex] == pattern[matched]) {
                    matched++;
                    if (matched == patternLength) {
                        return globalIndex + arrayIndex - patternLength + 1;
                    }
                } else {
                    arrayIndex = arrayIndex - matched; // 回溯到初始匹配位置的下一个位置
                    matched = 0;
                }
                arrayIndex++;
            }

            globalIndex += currentArray.length;
            currentArray = supplier.get();
            arrayIndex = 0;
        }

        //未找到模式
        return -1;
    }

}
