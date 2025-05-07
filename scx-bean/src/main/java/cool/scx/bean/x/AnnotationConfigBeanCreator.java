package cool.scx.bean.x;

import cool.scx.reflect.ClassInfoFactory;
import cool.scx.reflect.ConstructorInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.reflect.AccessModifier.PUBLIC;

/// 根据 class 创建 bean 
/// todo 此类待优化
public class AnnotationConfigBeanCreator implements BeanCreator {

    // 改成 List，保存依赖链路
    private static final ThreadLocal<List<AnnotationConfigBeanCreator>> CURRENTLY_CREATING = ThreadLocal.withInitial(ArrayList::new);

    private final Class<?> beanClass;
    private final ConstructorInfo constructor;

    public AnnotationConfigBeanCreator(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.constructor = initPreferredConstructor(beanClass);
    }

    public static ConstructorInfo initPreferredConstructor(Class<?> beanClass) {
        // todo 这里后期需要根据注解来解析 暂时先使用 第一个 public
        var classInfo = ClassInfoFactory.getClassInfo(beanClass);
        ConstructorInfo coreConstructor = null;
        for (var constructor : classInfo.constructors()) {
            if (constructor.accessModifier() == PUBLIC) {
                coreConstructor = constructor;
                break;
            }
        }
        if (coreConstructor == null) {
            throw new IllegalArgumentException("No constructor found for " + beanClass);
        } else {
            return coreConstructor;
        }
    }

    @Override
    public Class<?> beanClass() {
        return beanClass;
    }

    @Override
    public Object create(BeanFactory beanFactory) {
        var creatingList = CURRENTLY_CREATING.get();
        if (creatingList.contains(this)) {
            // 如果已存在，组装一条依赖链
            var cycle = new ArrayList<String>();
            for (var creator : creatingList) {
                cycle.add(creator.beanClass.getName());
            }
            cycle.add(this.beanClass.getName()); // 再加上自己形成完整回环
            var message = String.join(" -> ", cycle);
            throw new IllegalStateException("检测到循环依赖！依赖链 = " + message);
        }

        creatingList.add(this); // 加入正在创建列表
        try {
            var parameters = constructor.parameters();
            var objects = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                var parameter = parameters[i];
                for (var beanInjector : beanFactory.beanDependencyResolvers()) {
                    Object o = beanInjector.resolveConstructorArgument(parameter);
                    if (o != null) {
                        objects[i] = o;
                        break;
                    }
                }
            }
            return constructor.newInstance(objects);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            creatingList.remove(creatingList.size() - 1); // 创建结束，移除自己
        }
    }

}
