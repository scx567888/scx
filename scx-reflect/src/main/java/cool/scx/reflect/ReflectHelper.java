package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static cool.scx.common.util.ObjectUtils.resolveMemberType;
import static cool.scx.reflect.AccessModifier.PRIVATE;
import static cool.scx.reflect.ClassInfoFactory.getClassInfo;
import static cool.scx.reflect.ClassType.ENUM;
import static cool.scx.reflect.ClassType.RECORD;
import static java.util.Collections.addAll;

final class ReflectHelper {

    public static JavaType _findType(Type type, ClassInfo classInfo) {
        return resolveMemberType(type, classInfo.type().getBindings());
    }

    public static AccessModifier _findAccessModifier(Set<AccessFlag> accessFlags) {
        if (accessFlags.contains(AccessFlag.PUBLIC)) {
            return AccessModifier.PUBLIC;
        }
        if (accessFlags.contains(AccessFlag.PROTECTED)) {
            return AccessModifier.PROTECTED;
        }
        if (accessFlags.contains(AccessFlag.PRIVATE)) {
            return AccessModifier.PRIVATE;
        }
        return AccessModifier.PACKAGE_PRIVATE;
    }

    public static ClassType _findClassType(Class<?> rawClass, Set<AccessFlag> accessFlags) {
        if (accessFlags.contains(AccessFlag.ANNOTATION)) {
            return ClassType.ANNOTATION;
        }
        if (accessFlags.contains(AccessFlag.INTERFACE)) {
            return ClassType.INTERFACE;
        }
        if (accessFlags.contains(AccessFlag.ABSTRACT)) {
            return ClassType.ABSTRACT_CLASS;
        }
        if (accessFlags.contains(AccessFlag.ENUM)) {
            return ClassType.ENUM;
        }
        if (rawClass.isRecord()) {
            return RECORD;
        }
        return ClassType.CONCRETE;
    }

    public static MethodType _findMethodType(Method method, Set<AccessFlag> accessFlags) {
        if (accessFlags.contains(AccessFlag.ABSTRACT)) {
            return MethodType.ABSTRACT;
        }
        if (accessFlags.contains(AccessFlag.STATIC)) {
            return MethodType.STATIC;
        }
        if (method.isDefault()) {
            return MethodType.DEFAULT;
        }
        return MethodType.NORMAL;
    }

    public static ClassInfo _findSuperClass(JavaType type) {
        var superClass = type.getSuperClass();
        return superClass != null ? getClassInfo(superClass) : null;
    }

    public static ClassInfo[] _findInterfaces(JavaType type) {
        var interfaces = type.getInterfaces();
        var result = new ClassInfo[interfaces.size()];
        for (int i = 0; i < interfaces.size(); i = i + 1) {
            result[i] = getClassInfo(interfaces.get(i));
        }
        return result;
    }

    public static ConstructorInfo[] _findConstructorInfos(ClassInfo classInfo) {
        var constructors = classInfo.type().getRawClass().getDeclaredConstructors();
        var result = new ConstructorInfo[constructors.length];
        for (int i = 0; i < constructors.length; i = i + 1) {
            result[i] = new ConstructorInfo(constructors[i], classInfo);
        }
        return result;
    }

    /// 寻找 无参构造函数 (不支持成员类)
    ///
    /// @param classInfo c
    /// @return a
    public static ConstructorInfo _findDefaultConstructor(ClassInfo classInfo) {
        for (var constructor : classInfo.constructors()) {
            if (constructor.parameters().length == 0) {
                return constructor;
            }
        }
        return null;
    }

    /// 寻找 Record 规范构造参数
    public static ConstructorInfo _findRecordConstructor(ClassInfo classInfo) {
        if (classInfo.classType() != RECORD) {
            return null;
        }
        var recordComponentTypes = _getRecordComponentsTypes(classInfo);
        for (var constructor : classInfo.constructors()) {
            // 判断参数类型是否匹配
            var matched = _hasSameParameterTypes(constructor, recordComponentTypes);
            if (matched) {
                return constructor;
            }
        }
        return null;
    }

    public static FieldInfo[] _findFieldInfos(ClassInfo classInfo) {
        var fields = classInfo.type().getRawClass().getDeclaredFields();
        var result = new FieldInfo[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            result[i] = new FieldInfo(fields[i], classInfo);
        }
        return result;
    }

    public static FieldInfo[] _findAllFieldInfos(ClassInfo classInfo) {
        var allFieldInfos = new ArrayList<FieldInfo>();
        while (classInfo != null) {
            addAll(allFieldInfos, classInfo.fields());
            classInfo = classInfo.superClass();
        }
        return allFieldInfos.toArray(FieldInfo[]::new);
    }

    /// 获取当前 ClassInfo 的所有方法 (不包括父类方法 不包括桥接方法)
    ///
    /// @param classInfo c
    /// @return c
    public static MethodInfo[] _findMethodInfos(ClassInfo classInfo) {
        var methods = classInfo.type().getRawClass().getDeclaredMethods();
        var list = new ArrayList<MethodInfo>();
        for (var method : methods) {
            if (!method.isBridge()) {
                list.add(new MethodInfo(method, classInfo));
            }
        }
        return list.toArray(MethodInfo[]::new);
    }

    public static MethodInfo[] _findAllMethodInfos(ClassInfo classInfo) {
        //存储所有出现过的父类方法 用于过滤
        var filter = new HashSet<MethodInfo>();
        var allMethodInfo = new ArrayList<MethodInfo>();
        //这里 排除 Object 的所有方法
        while (classInfo != null) {
            var methods = classInfo.methods();
            for (var method : methods) {
                //如果有上层的重写方法 添加到 superMethodSet 中 在下一次循环中以便过滤
                if (method.superMethod() != null) {
                    filter.add(method.superMethod());
                }
                var b = filter.contains(method);
                if (!b) {
                    allMethodInfo.add(method);
                }
            }
            classInfo = classInfo.superClass();
        }
        return allMethodInfo.toArray(MethodInfo[]::new);
    }

    /// 获取当前方法的注解 同时包含 重写方法的注解
    ///
    /// @return a
    public static Annotation[] _findAllAnnotations(ClassInfo classInfo) {
        var allAnnotations = new ArrayList<Annotation>();
        while (classInfo != null) {
            addAll(allAnnotations, classInfo.annotations());
            classInfo = classInfo.superClass();
        }
        return allAnnotations.toArray(Annotation[]::new);
    }

    public static ClassInfo _findEnumClass(ClassInfo classInfo) {
        if (classInfo.classType() == ENUM) {
            return classInfo.isAnonymousClass() ? classInfo.superClass() : classInfo;
        } else {
            return null;
        }
    }

    public static ClassInfo _findComponentType(ClassInfo classInfo) {
        if (classInfo.isArray()) {
            return getClassInfo(classInfo.type().getRawClass().componentType());
        } else {
            return null;
        }
    }

    private static JavaType[] _getRecordComponentsTypes(ClassInfo classInfo) {
        var recordComponents = classInfo.type().getRawClass().getRecordComponents();
        var result = new JavaType[recordComponents.length];
        for (int i = 0; i < recordComponents.length; i = i + 1) {
            result[i] = _findType(recordComponents[i].getGenericType(), classInfo);
        }
        return result;
    }

    public static ParameterInfo[] _findParameterInfos(ConstructorInfo constructorInfo) {
        var parameters = constructorInfo.constructor().getParameters();
        var result = new ParameterInfo[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            result[i] = new ParameterInfo(parameters[i], constructorInfo);
        }
        return result;
    }

    public static ParameterInfo[] _findParameterInfos(MethodInfo methodInfo) {
        var parameters = methodInfo.method().getParameters();
        var result = new ParameterInfo[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            result[i] = new ParameterInfo(parameters[i], methodInfo);
        }
        return result;
    }

    public static MethodInfo _findSuperMethod(MethodInfo methodInfo) {
        var superClass = methodInfo.classInfo().superClass();
        while (superClass != null) {
            var superMethods = superClass.methods();
            for (var superMethod : superMethods) {
                var b = isOverride(methodInfo, superMethod);
                // 只查找第一次匹配的方法 
                if (b) {
                    return superMethod;
                }
            }
            superClass = superClass.superClass();
        }
        return null;
    }

    /// 获取当前方法的注解 同时包含 重写方法的注解
    ///
    /// @return a
    public static Annotation[] _findAllAnnotations(MethodInfo methodInfo) {
        var allAnnotations = new ArrayList<Annotation>();
        while (methodInfo != null) {
            addAll(allAnnotations, methodInfo.annotations());
            methodInfo = methodInfo.superMethod();
        }
        return allAnnotations.toArray(Annotation[]::new);
    }

    /// 判断是否为重写方法
    ///
    /// @param rootMethod      a
    /// @param candidateMethod a
    /// @return a
    private static boolean isOverride(MethodInfo rootMethod, MethodInfo candidateMethod) {
        return PRIVATE != candidateMethod.accessModifier() &&
                candidateMethod.name().equals(rootMethod.name()) &&
                _hasSameParameterTypes(rootMethod, candidateMethod);
    }

    private static boolean _hasSameParameterTypes(ExecutableInfo rootMethod, ExecutableInfo candidateMethod) {
        if (candidateMethod.parameters().length != rootMethod.parameters().length) {
            return false;
        }
        var p1 = rootMethod.parameters();
        var p2 = candidateMethod.parameters();
        for (int i = 0; i < p1.length; i = i + 1) {
            var p1Type = p1[i].type().getRawClass();
            var p2Type = p2[i].type().getRawClass();
            if (p1Type != p2Type) {
                return false;
            }
        }
        return true;
    }

    private static boolean _hasSameParameterTypes(ExecutableInfo constructorInfo, JavaType[] types) {
        if (constructorInfo.parameters().length != types.length) {
            return false;
        }
        var p1 = constructorInfo.parameters();
        for (int i = 0; i < p1.length; i = i + 1) {
            var p1Type = p1[i].type().getRawClass();
            var p2Type = types[i].getRawClass();
            if (p1Type != p2Type) {
                return false;
            }
        }
        return true;
    }

}
