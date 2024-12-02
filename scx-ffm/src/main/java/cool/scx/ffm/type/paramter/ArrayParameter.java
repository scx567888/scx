package cool.scx.ffm.type.paramter;

import cool.scx.ffm.type.mapper.Mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.reflect.Array;

/**
 * ArrayParameter
 *
 * @author scx567888
 * @version 0.0.1
 */
public class ArrayParameter implements Parameter {

    private final Object array;
    private final Mapper arrayMapper;
    private MemorySegment memorySegment;

    public ArrayParameter(Object array, Mapper arrayMapper) {
        this.array = array;
        this.arrayMapper = arrayMapper;
    }

    @Override
    public Object toNativeParameter(Arena arena) {
        return this.memorySegment = arrayMapper.toMemorySegment(arena);
    }

    @Override
    public void beforeCloseArena() {
        var result = arrayMapper.fromMemorySegment(memorySegment);
        //此处为了模拟 数组的引用 形式
        //为了使 外部的 数组对象[] 引用依然可用 这里使用 arraycopy 
        System.arraycopy(result, 0, array, 0, Array.getLength(array));
    }

}
