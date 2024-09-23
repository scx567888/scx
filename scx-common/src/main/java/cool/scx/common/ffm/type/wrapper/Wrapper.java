package cool.scx.common.ffm.type.wrapper;

import cool.scx.common.ffm.type.paramter.Parameter;
import cool.scx.common.ffm.type.mapper.Mapper;

import java.lang.foreign.Arena;

/**
 * 创建动机是为了规范化一些参数类型
 * 此参数类型其实可以直接被替换成对应的基本类型
 * 将其理解为只读 如果需要在外部函数中修改 请使用 {@link Mapper}
 */
public interface Wrapper<V> extends Parameter {

    V getValue();

    @Override
    default Object toNativeParameter(Arena arena) {
        return getValue();
    }

}
