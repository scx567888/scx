package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;
import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.ClassInfoFactory;

import static cool.scx.bean.provider.InjectingBeanProvider.InjectionPolicy.NEVER;
import static cool.scx.bean.provider.InjectingBeanProvider.InjectionPolicy.ONCE;

/// 支持字段和方法注入 的 提供器
public class InjectingBeanProvider implements BeanProvider {

    private final BeanProvider beanProvider;
    private InjectionPolicy injectionPolicy;

    public InjectingBeanProvider(BeanProvider beanProvider, InjectionPolicy injectionPolicy) {
        this.beanProvider = beanProvider;
        this.injectionPolicy = injectionPolicy;
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {
        var bean = beanProvider.getBean(beanFactory);
        if (injectionPolicy == NEVER) {
            return bean;
        }
        if (injectionPolicy == ONCE) {
            injectionPolicy = NEVER; // 修改策略：从 ONCE 转为 NEVER, 防止再次注入, 同时先修改状态 再执行注入也是为了支持 循环注入
        }
        injectFieldAndMethod(bean, beanFactory);
        return bean;
    }

    @Override
    public Class<?> beanClass() {
        return beanProvider.beanClass();
    }

    private void injectFieldAndMethod(Object bean, BeanFactory beanFactory) {
        var classInfo = ClassInfoFactory.getClassInfo(beanClass());
        var fieldInfos = classInfo.allFields();
        for (var fieldInfo : fieldInfos) {
            //只处理 public 字段
            if (fieldInfo.accessModifier() == AccessModifier.PUBLIC) {
                fieldInfo.setAccessible(true);
                for (var resolver : beanFactory.beanResolvers()) {
                    var fieldValue = resolver.resolveFieldValue(fieldInfo);
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
                for (var resolver : beanFactory.beanResolvers()) {
                    var b = resolver.resolveMethod(methodInfo);
                    if (b) {
                        break;
                    }
                }
            }
        }
    }

    public enum InjectionPolicy {
        
        /// 每次都注入
        ALWAYS,
        
        /// 注入一次
        ONCE,
        
        /// 从不注入
        NEVER

    }

}
