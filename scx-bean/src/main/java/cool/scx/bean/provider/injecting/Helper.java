package cool.scx.bean.provider.injecting;

import cool.scx.bean.BeanFactory;
import cool.scx.reflect.FieldInfo;

class Helper {

    /// 解析构造函数参数
    public static Object resolveFieldValue(BeanFactory beanFactory, FieldInfo fieldInfo) {
        for (var beanResolver : beanFactory.beanResolvers()) {
            var value = beanResolver.resolveFieldValue(fieldInfo);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

}
