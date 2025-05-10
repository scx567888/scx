package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;
import cool.scx.bean.dependency.DependencyContext;
import cool.scx.bean.exception.BeanCreationException;
import cool.scx.reflect.ClassInfoFactory;
import cool.scx.reflect.FieldInfo;

import static cool.scx.bean.dependency.CircularDependencyChecker.*;
import static cool.scx.bean.dependency.DependencyContext.Type.CONSTRUCTOR;
import static cool.scx.bean.provider.InjectingBeanProvider.BeanStatus.*;
import static cool.scx.reflect.AccessModifier.PUBLIC;

/// 支持字段和方法注入 的 提供器
public class InjectingBeanProvider implements BeanProvider {

    private final BeanProvider beanProvider;
    private BeanStatus beanStatus;

    public InjectingBeanProvider(BeanProvider beanProvider) {
        this.beanProvider = beanProvider;
        this.beanStatus = NULL;
    }

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

    /// 判断是否可以返回早期对象
    /// 此方法核心逻辑和 CircularDependencyChecker 中检查循环依赖是否可解很相似
    private static boolean shouldReturnEarly() {
        //如果 没有调用链条 说明是 (用户调用), 我们 不支持 早期对象
        var dependencyChain = getCurrentDependencyChain();
        if (dependencyChain.isEmpty()) {
            return false;
        }
        // 有链条而且链条上 有任意一个构造函数 我们 不支持 早期对象
        // 因为如果此时返回早期对象就会导致 构造函数中拿到的不是一个 完全体 用户调用时可能引发空指针
        for (var context : dependencyChain) {
            if (context.type() == CONSTRUCTOR) {
                return false;
            }
        }
        //没有任何一个 构造函数链条 那就是 全是 字段链条
        return true;
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {
        var bean = beanProvider.getBean(beanFactory);
        // 单例模式只注入一遍
        if (beanProvider.singleton()) {
            //已经注入 直接返回
            if (beanStatus == READY) {
                return bean;
            }
            //半注入状态 需要判断是否应该返回早期对象
            if (beanStatus == INJECTING && shouldReturnEarly()) {
                return bean;
            }
            //这里提前设置 在第二次调用时 就可以暴漏半成品
            beanStatus = INJECTING;
            injectField(bean, beanFactory);
            beanStatus = READY;
        } else {
            //开始注入
            injectField(bean, beanFactory);
        }
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

    private void injectField(Object bean, BeanFactory beanFactory) throws BeanCreationException {
        var classInfo = ClassInfoFactory.getClassInfo(beanClass());
        var fieldInfos = classInfo.allFields();

        for (var fieldInfo : fieldInfos) {
            //只处理非 final 的 public 字段
            if (fieldInfo.accessModifier() != PUBLIC || fieldInfo.isFinal()) {
                continue;
            }

            //开始检查依赖
            startDependencyCheck(new DependencyContext(this.beanClass(), this.singleton(), fieldInfo));

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

    public enum BeanStatus {
        NULL,      // 未开始初始化
        INJECTING, // 正在注入依赖
        READY      // 完全就绪
    }

}
