package cool.scx.reflect.i;

import java.lang.reflect.WildcardType;

import static cool.scx.reflect.i.ScxReflect.TYPE_INFO_CACHE;

//todo 待完成
public class WildcardTypeInfoImpl implements WildcardTypeInfo {

    WildcardTypeInfoImpl(WildcardType wildcardType) {
        TYPE_INFO_CACHE.put(wildcardType, this);
    }

}
