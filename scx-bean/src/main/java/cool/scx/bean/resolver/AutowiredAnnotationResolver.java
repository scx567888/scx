package cool.scx.bean.resolver;

import cool.scx.bean.BeanFactory;
import cool.scx.bean.annotation.Autowired;
import cool.scx.bean.exception.NoSuchBeanException;
import cool.scx.bean.exception.NoUniqueBeanException;
import cool.scx.common.constant.AnnotationValueHelper;
import cool.scx.reflect.AnnotatedElementInfo;
import cool.scx.reflect.FieldInfo;
import cool.scx.reflect.ParameterInfo;

/// 处理 Autowired 注解 同时也承担最核心的 配置
///
/// @author scx567888
/// @version 0.0.1
public class AutowiredAnnotationResolver implements BeanResolver {

    private final BeanFactory beanFactory;

    public AutowiredAnnotationResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValue(AnnotatedElementInfo annotatedElementInfo, Class<?> clazz) throws NoSuchBeanException, NoUniqueBeanException {
        //只处理有 Autowired 注解的
        var autowired = annotatedElementInfo.findAnnotation(Autowired.class);
        if (autowired == null) {
            return null;
        }
        var name = AnnotationValueHelper.getRealValue(autowired.value());
        if (name != null) {
            return beanFactory.getBean(name, clazz);
        } else {
            return beanFactory.getBean(clazz);
        }
    }

    @Override
    public Object resolveConstructorArgument(ParameterInfo parameter) {
        var annotations = parameter.annotations();
        // 构造参数和 fieldValue 规则略有不同, 没有任何其他注解 强制注入
        if (annotations.length == 0) {
            return beanFactory.getBean(parameter.rawParameter().getType());
        }
        return resolveValue(parameter, parameter.rawParameter().getType());
    }

    @Override
    public Object resolveFieldValue(FieldInfo fieldInfo) {
        return resolveValue(fieldInfo, fieldInfo.rawField().getType());
    }

}
