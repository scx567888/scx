package cool.scx.common.util;

import java.util.function.Supplier;

public class ArrayFinder {

    public static int indexOf(Supplier<byte[]> supplier, byte[] pattern) {

        //1, 检查模式是否为空或长度为零
        if (pattern == null || pattern.length == 0) {
            return -1;
        }

        int patternLength = pattern.length;
        int globalIndex = 0; // 跟踪整体扫描的位置
        int matched = 0; // 跟踪当前匹配的字节数

        // 使用 Supplier<byte[]> 来逐步获取新数组，直到没有更多数组可获取（即返回 null）。
        byte[] currentArray = supplier.get();
        int arrayIndex = 0;

        while (currentArray != null) {
            while (arrayIndex < currentArray.length) {
                if (currentArray[arrayIndex] == pattern[matched]) {
                    matched++;
                    if (matched == patternLength) {
                        return globalIndex + arrayIndex - patternLength + 1;
                    }
                    arrayIndex++;
                    if (arrayIndex == currentArray.length) {
                        globalIndex += arrayIndex;
                        currentArray = supplier.get();
                        arrayIndex = 0;
                    }
                } else {
                    arrayIndex = arrayIndex - matched + 1; // 回溯到匹配的起始位置的下一个位置
                    if (arrayIndex < 0) {
                        arrayIndex = 0;
                    }
                    matched = 0;
                }
            }

            globalIndex += currentArray.length;
            currentArray = supplier.get();
            arrayIndex = 0;
        }

        //未找到模式
        return -1;
    }
    
}
