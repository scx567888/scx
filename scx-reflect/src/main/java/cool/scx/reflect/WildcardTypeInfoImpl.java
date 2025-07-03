package cool.scx.reflect;

import java.lang.reflect.WildcardType;

import static cool.scx.reflect.ScxReflect.TYPE_INFO_CACHE;

//todo 待完成
public class WildcardTypeInfoImpl implements WildcardTypeInfo {

    WildcardTypeInfoImpl(WildcardType wildcardType) {
        TYPE_INFO_CACHE.put(wildcardType, this);
    }

}
