package cool.scx.bean.x;

import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.ClassInfoFactory;

public class BeanHelper {

    public static void injectFieldAndMethod(Object bean, Class<?> objectClass, BeanFactory beanFactory) {
        var classInfo = ClassInfoFactory.getClassInfo(objectClass);
        var fieldInfos = classInfo.allFields();
        for (var fieldInfo : fieldInfos) {
            //只处理 public 字段
            if (fieldInfo.accessModifier() == AccessModifier.PUBLIC) {
                fieldInfo.setAccessible(true);
                for (var injector : beanFactory.beanInjectors()) {
                    var fieldValue = injector.resolveFieldValue(fieldInfo);
                    if (fieldValue != null) {
                        try {
                            fieldInfo.set(bean, fieldValue);
                            break;
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("注入 Field 异常 !!!", e);
                        }
                    }
                }
            }
        }
        var methodInfos = classInfo.allMethods();
        for (var methodInfo : methodInfos) {
            //只处理 public 方法
            if (methodInfo.accessModifier() == AccessModifier.PUBLIC) {
                methodInfo.setAccessible(true);
                for (var injector : beanFactory.beanInjectors()) {
                    var b = injector.resolveMethod(methodInfo);
                    if (b) {
                        break;
                    }
                }
            }
        }
    }

}
