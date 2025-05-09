package cool.scx.bean.provider;

import cool.scx.bean.BeanFactory;
import cool.scx.bean.annotation.PreferredConstructor;
import cool.scx.bean.exception.BeanCreationException;
import cool.scx.bean.exception.NoSuchConstructorException;
import cool.scx.bean.exception.NoUniqueConstructorException;
import cool.scx.reflect.ClassInfoFactory;
import cool.scx.reflect.ConstructorInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static cool.scx.reflect.AccessModifier.PUBLIC;

/// 根据 class 创建 Bean
public class AnnotationConfigBeanProvider implements BeanProvider {

    // 保存依赖链路
    private static final ThreadLocal<List<Class<?>>> CURRENTLY_CREATING = ThreadLocal.withInitial(ArrayList::new);

    private final Class<?> beanClass;
    private final ConstructorInfo constructor;

    public AnnotationConfigBeanProvider(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.constructor = findPreferredConstructor(beanClass);
    }

    public static ConstructorInfo findPreferredConstructor(Class<?> beanClass) {
        // 我们只使用 public 的 构造函数
        var classInfo = ClassInfoFactory.getClassInfo(beanClass);
        var publicConstructors = new ArrayList<ConstructorInfo>();
        for (var constructor : classInfo.constructors()) {
            if (constructor.accessModifier() == PUBLIC) {
                publicConstructors.add(constructor);
            }
        }
        
        //一个都没有 报错
        if (publicConstructors.isEmpty()) {
            throw new NoSuchConstructorException("无法找到类 " + beanClass.getName() + " 的任何 public 构造方法," + "至少需要一个 public 构造方法用于创建 Bean.");
        }
        
        //只找到一个直接用
        if (publicConstructors.size() == 1) {
            return publicConstructors.get(0);
        }
        
        //找到多个 需要查看是否有切只有一个标注了 PreferredConstructor 注解的
        var preferredConstructors = new ArrayList<ConstructorInfo>();
        for (var constructorInfo : publicConstructors) {
            if (constructorInfo.findAnnotation(PreferredConstructor.class) != null) {
                preferredConstructors.add(constructorInfo);
            }
        }
        if (preferredConstructors.isEmpty()) {
            throw new NoUniqueConstructorException(
                    "在类 " + beanClass.getName() + " 中检测到多个 public 构造方法，且都未标注 @PreferredConstructor 注解," +
                            "无法确定应使用哪个构造方法。\n" +
                            "可用的 public 构造方法列表：\n" + formatConstructors(publicConstructors) +
                            "\n请在期望使用的构造方法上添加 @PreferredConstructor 注解。"
            );
        }
        if (preferredConstructors.size() == 1) {
            return preferredConstructors.get(0);
        }

        throw new NoUniqueConstructorException(
                "在类 " + beanClass.getName() + " 中检测到多个标注了 @PreferredConstructor 注解的 public 构造方法, " +
                        "无法唯一确定使用哪个构造方法。\n" +
                        "冲突的构造方法列表：\n" + formatConstructors(preferredConstructors) +
                        "\n同一个类中只能有一个构造方法标注 @PreferredConstructor，请检查修正."
        );
        
    }

    private static String formatConstructors(List<ConstructorInfo> constructors) {
        var builder = new StringBuilder();
        for (var constructor : constructors) {
            builder.append("  - ")
                    .append(constructor.constructor().toGenericString())
                    .append("\n");
        }
        return builder.toString();
    }

    public static String buildCycleText(List<Class<?>> creatingList, Class<?> beanClass) {
        // 如果已存在，组装一条依赖链
        var cycle = new ArrayList<String>();
        for (var creator : creatingList) {
            cycle.add(creator.getName());
        }
        cycle.add(beanClass.getName()); // 再加上自己形成完整回环
        return String.join(" -> ", cycle);
    }

    public Object newInstance(BeanFactory beanFactory) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        //1, 获取参数
        var parameters = constructor.parameters();
        //2, 创建参数数组
        var objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];
            //3, 尝试从 beanResolver 中获取 
            for (var beanResolver : beanFactory.beanResolvers()) {
                var value = beanResolver.resolveConstructorArgument(parameter);
                if (value != null) {
                    objects[i] = value;
                    break;
                }
            }
        }
        return constructor.newInstance(objects);
    }

    @Override
    public Class<?> beanClass() {
        return beanClass;
    }

    @Override
    public Object getBean(BeanFactory beanFactory) {
        var creatingList = CURRENTLY_CREATING.get();

        // 检测循环依赖
        if (creatingList.contains(this.beanClass)) {
            var message = buildCycleText(creatingList, this.beanClass);
            throw new BeanCreationException("检测到构造函数循环依赖, 依赖链 = [" + message + "]");
        }

        creatingList.add(this.beanClass); // 加入正在创建列表
        // 创建实例
        try {
            return newInstance(beanFactory);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeanCreationException("创建 bean 时发生异常 ", e);
        } catch (InvocationTargetException e) {
            throw new BeanCreationException("创建 bean 时发生异常 ", e.getCause());
        } finally {
            creatingList.removeLast();
        }
    }

    @Override
    public boolean singleton() {
        return false;
    }

}
