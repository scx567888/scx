package cool.scx.bean.provider.annotation_config;

import cool.scx.bean.BeanFactory;
import cool.scx.bean.annotation.PreferredConstructor;
import cool.scx.bean.exception.NoSuchConstructorException;
import cool.scx.bean.exception.NoUniqueConstructorException;
import cool.scx.reflect.ClassInfoFactory;
import cool.scx.reflect.ConstructorInfo;
import cool.scx.reflect.ParameterInfo;

import java.util.ArrayList;
import java.util.List;

import static cool.scx.reflect.AccessModifier.PUBLIC;

class Helper {

    /// 查找构造函数
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

    /// 美化构造函数输出
    private static String formatConstructors(List<ConstructorInfo> constructors) {
        var builder = new StringBuilder();
        for (var constructor : constructors) {
            builder.append("  - ")
                    .append(constructor.constructor().toGenericString())
                    .append("\n");
        }
        return builder.toString();
    }

    /// 解析构造函数参数
    public static Object resolveConstructorArgument(BeanFactory beanFactory, ParameterInfo parameter) {
        for (var beanResolver : beanFactory.beanResolvers()) {
            var value = beanResolver.resolveConstructorArgument(parameter);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

}
