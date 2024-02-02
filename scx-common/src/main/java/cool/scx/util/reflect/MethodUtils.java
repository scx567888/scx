package cool.scx.util.reflect;

import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Collections.addAll;

public class MethodUtils {

    /**
     * 寻找给定 class 及其所有父类(不包含 Object)中所有的方法
     *
     * @param clazz c
     * @return m
     */
    public static Method[] findAllMethods(Class<?> clazz) {
        var list = new ArrayList<Method>();
        while (clazz != null && clazz != Object.class) {
            var methods = clazz.getDeclaredMethods();
            addAll(list, methods);
            clazz = clazz.getSuperclass();
        }
        return list.toArray(Method[]::new);
    }

    /**
     * 寻找给定 class 及其继承的(不包含 Object)中所有的 "public" 方法
     *
     * @param clazz c
     * @return c
     */
    public static Method[] findMethods(Class<?> clazz) {
        var list = new ArrayList<Method>();
        var methods = clazz.getMethods();
        for (var m : methods) {
            if (m.getDeclaringClass() != Object.class) {
                list.add(m);
            }
        }
        return list.toArray(Method[]::new);
    }

    /**
     * 寻找指定方法的所有注解 (会继承父类方法的注解)
     *
     * @param sourceMethod s
     * @return s
     */
    public static Annotation[] findAllAnnotations(Method sourceMethod) {
        var list = new ArrayList<Annotation>();
        var sourceClass = sourceMethod.getDeclaringClass();
        var methods = findAllMethods(sourceClass);
        for (var m : methods) {
            var isOverride = isOverride(sourceMethod, m);
            if (isOverride) {
                addAll(list, m.getDeclaredAnnotations());
            }
        }
        return list.toArray(Annotation[]::new);
    }

    private static boolean isOverride(Method rootMethod, Method candidateMethod) {
        return (!Modifier.isPrivate(candidateMethod.getModifiers()) &&
                candidateMethod.getName().equals(rootMethod.getName()) &&
                hasSameParameterTypes(rootMethod, candidateMethod));
    }

    private static boolean hasSameParameterTypes(Method rootMethod, Method candidateMethod) {
        if (candidateMethod.getParameterCount() != rootMethod.getParameterCount()) {
            return false;
        }
        Class<?>[] rootParameterTypes = rootMethod.getParameterTypes();
        Class<?>[] candidateParameterTypes = candidateMethod.getParameterTypes();
        if (Arrays.equals(candidateParameterTypes, rootParameterTypes)) {
            return true;
        }
        return hasSameGenericTypeParameters(rootMethod, candidateMethod,
                rootParameterTypes);
    }

    private static boolean hasSameGenericTypeParameters(
            Method rootMethod, Method candidateMethod, Class<?>[] rootParameterTypes) {

        Class<?> sourceDeclaringClass = rootMethod.getDeclaringClass();
        Class<?> candidateDeclaringClass = candidateMethod.getDeclaringClass();
        if (!candidateDeclaringClass.isAssignableFrom(sourceDeclaringClass)) {
            return false;
        }
        for (int i = 0; i < rootParameterTypes.length; i++) {
            Class<?> resolvedParameterType = ResolvableType.forMethodParameter(
                    candidateMethod, i, sourceDeclaringClass).resolve();
            if (rootParameterTypes[i] != resolvedParameterType) {
                return false;
            }
        }
        return true;
    }

}
