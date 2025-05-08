package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;
import cool.scx.reflect.AccessModifier;
import cool.scx.reflect.ClassInfoFactory;

import java.util.ArrayList;
import java.util.List;

/// 支持字段和方法注入 的 提供器
public class InjectingBeanProvider implements BeanProvider {

    // 保存依赖链路
    private static final ThreadLocal<List<InjectingBeanProvider>> CURRENTLY_CREATING = ThreadLocal.withInitial(ArrayList::new);

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

        var creatingList = CURRENTLY_CREATING.get();

        // 检测循环依赖
        if (checkContains(creatingList, this)) {
            boolean b = checkAllArePrototype(creatingList);
            if (b) { // 多例
                var message = buildCycleText(creatingList, this);
                throw new IllegalStateException("检测到字段循环依赖（多例禁止），依赖链 = [" + message + "]");
            }
        }

        creatingList.add(this); // 加入正在创建列表
        try {
            injectFieldAndMethod(bean, beanFactory);
        } finally {
            creatingList.removeLast();
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

    public static boolean checkContains(List<InjectingBeanProvider> creatingList, InjectingBeanProvider injectingBeanProvider) {
        for (InjectingBeanProvider beanProvider : creatingList) {
            if (beanProvider.beanClass() == injectingBeanProvider.beanClass()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkAllArePrototype(List<InjectingBeanProvider> creatingList) {
        for (var beanClass : creatingList) {
            if (beanClass.singleton()) {
                return false;
            }
        }
        return true;
    }

    public static String buildCycleText(List<InjectingBeanProvider> creatingList, InjectingBeanProvider beanClass) {
        // 如果已存在，组装一条依赖链
        var cycle = new ArrayList<String>();
        for (var creator : creatingList) {
            cycle.add(creator.beanClass().getName());
        }
        cycle.add(beanClass.beanClass().getName()); // 再加上自己形成完整回环
        return String.join(" -> ", cycle);
    }

}
