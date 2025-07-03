package cool.scx.reflect;

import java.lang.reflect.TypeVariable;

import static cool.scx.reflect.ScxReflect.TYPE_INFO_CACHE;

//todo 待完成
public class TypeVariableInfoImpl implements TypeVariableInfo {

    TypeVariableInfoImpl(TypeVariable<?> typeVariable) {
        TYPE_INFO_CACHE.put(typeVariable, this);
    }

}
