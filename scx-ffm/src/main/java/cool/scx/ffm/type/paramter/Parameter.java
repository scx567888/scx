package cool.scx.ffm.type.paramter;

import java.lang.foreign.Arena;

public interface Parameter {

    Object toNativeParameter(Arena arena);

    /**
     * 在即将退出 arena 范围之前执行
     * 对于一些被外部修改的方法 可以在此处 将外部内存段中的值 写入到 java 内存中
     */
    default void beforeCloseArena() {

    }

}
