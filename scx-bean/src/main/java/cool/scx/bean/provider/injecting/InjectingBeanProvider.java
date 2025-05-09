package cool.scx.bean.provider.injecting;

import cool.scx.bean.BeanFactory;
import cool.scx.bean.exception.BeanCreationException;
import cool.scx.bean.provider.BeanProvider;
import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.ClassInfoFactory;

import static cool.scx.bean.provider.injecting.CircularDependencyChecker.endDependencyCheck;
import static cool.scx.bean.provider.injecting.CircularDependencyChecker.startDependencyCheck;
import static cool.scx.bean.provider.injecting.Helper.resolveFieldValue;

/// 支持字段和方法注入 的 提供器
public class InjectingBeanProvider implements BeanProvider {

    private final BeanProvider beanProvider;
    private boolean alreadyInjected;

    public InjectingBeanProvider(BeanProvider beanProvider) {
        this.beanProvider = beanProvider;
        this.alreadyInjected = false;
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {
        var bean = beanProvider.getBean(beanFactory);
        // 单例模式
        if (beanProvider.singleton()) {
            //已经注入 直接返回
            if (alreadyInjected) {
                return bean;
            }
            alreadyInjected = true;
        }

        injectField(bean, beanFactory);
        injectMethod(bean, beanFactory);

        return bean;
    }

    @Override
    public boolean singleton() {
        return beanProvider.singleton();
    }

    @Override
    public Class<?> beanClass() {
        return beanProvider.beanClass();
    }

    private void injectField(Object bean, BeanFactory beanFactory) {
        var classInfo = ClassInfoFactory.getClassInfo(beanClass());
        var fieldInfos = classInfo.allFields();

        for (var fieldInfo : fieldInfos) {
            //只处理 public 字段
            if (fieldInfo.accessModifier() != AccessModifier.PUBLIC) {
                continue;
            }
            fieldInfo.setAccessible(true);

            //开始检查依赖
            startDependencyCheck(new DependentContext(this.beanClass(), this.singleton(), fieldInfo));

            try {
                var value = resolveFieldValue(beanFactory, fieldInfo);
                //只设置非空值
                if (value != null) {
                    fieldInfo.set(bean, value);
                }
            } catch (Exception e) {
                throw new BeanCreationException("在类 " + beanClass().getName() + " 中, 注入字段 [" + fieldInfo.name() + "] 阶段发生异常 !!!", e);
            } finally {
                //结束检查
                endDependencyCheck();
            }

        }
    }

    private void injectMethod(Object bean, BeanFactory beanFactory) {
        //todo 暂未实现方法注入
    }

}
