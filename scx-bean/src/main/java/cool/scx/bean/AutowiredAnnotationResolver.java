package cool.scx.bean;

import cool.scx.bean.annotation.Autowired;
import cool.scx.reflect.FieldInfo;
import cool.scx.reflect.MethodInfo;
import cool.scx.reflect.ParameterInfo;

import java.lang.annotation.Annotation;

/// 处理 Autowired 注解 同时也承担最核心的 配置
public class AutowiredAnnotationResolver implements BeanDependencyResolver {

    private final BeanFactory beanFactory;

    public AutowiredAnnotationResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object resolveConstructorArgument(ParameterInfo parameter) {
        Annotation[] annotations = parameter.allAnnotations();
        // 没有任何其他注解我们就当作是必须设置的, 强制获取
        if (annotations.length == 0) {
            return beanFactory.getBean(parameter.parameter().getType());
        } else {
            // 只有一个注解 并且这个注解 还是 Autowired, 一样强制获取
            if (annotations.length == 1 && annotations[0] instanceof Autowired autowired) {
                return beanFactory.getBean(parameter.parameter().getType());
            }
        }
        return null;
    }

    @Override
    public Object resolveFieldValue(FieldInfo fieldInfo) {
        //只处理有 Autowired 注解的
        var annotation = fieldInfo.findAnnotation(Autowired.class);
        if (annotation == null) {
            return null;
        }
        var type = fieldInfo.field().getType();
        return beanFactory.getBean(type);
    }

    @Override
    public boolean resolveMethod(MethodInfo methodInfo) {
        return false;
    }

}
