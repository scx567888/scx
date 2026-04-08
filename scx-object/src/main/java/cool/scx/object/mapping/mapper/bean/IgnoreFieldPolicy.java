package cool.scx.object.mapping.mapper.bean;

import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.FieldInfo;

public interface IgnoreFieldPolicy {

    boolean needIgnore(ClassInfo classInfo, FieldInfo fieldInfo);

}
