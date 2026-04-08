package cool.scx.object.mapping.mapper.bean;

import cool.scx.object.mapping.NodeMapperOptions;
import cool.scx.reflect.ClassInfo;
import cool.scx.reflect.FieldInfo;

import java.util.ArrayList;
import java.util.List;

public final class BeanNodeMapperOptions implements NodeMapperOptions {

    private boolean ignoreNullValue;
    private List<IgnoreFieldPolicy> ignoreFieldPolicyList;

    public BeanNodeMapperOptions() {
        this.ignoreNullValue = false;
    }

    /// 是否忽略 null 值, 多用于 Map 和 Object
    public boolean ignoreNullValue() {
        return ignoreNullValue;
    }

    public BeanNodeMapperOptions ignoreNullValue(boolean ignoreNullValue) {
        this.ignoreNullValue = ignoreNullValue;
        return this;
    }

    public BeanNodeMapperOptions ignoreFieldPolicy(IgnoreFieldPolicy ignoreFieldPolicy) {
        if (ignoreFieldPolicyList == null) {
            ignoreFieldPolicyList = new ArrayList<>();
        }
        ignoreFieldPolicyList.add(ignoreFieldPolicy);
        return this;
    }

    boolean needIgnore(ClassInfo classInfo, FieldInfo fieldInfo) {
        if (ignoreFieldPolicyList != null) {
            for (var ignoreFieldPolicy : ignoreFieldPolicyList) {
                var f = ignoreFieldPolicy.needIgnore(classInfo, fieldInfo);
                if (f) {
                    return true;
                }
            }
        }
        return false;
    }

}
