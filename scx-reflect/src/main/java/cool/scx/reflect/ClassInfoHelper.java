package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessFlag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static cool.scx.reflect.ClassType.ENUM;
import static cool.scx.reflect.ClassType.RECORD;
import static cool.scx.reflect.ReflectHelper._findType;
import static cool.scx.reflect.ReflectHelper.getClassInfo;
import static java.util.Collections.addAll;


/// ClassInfoHelper
///
/// @author scx567888
/// @version 0.0.1
final class ClassInfoHelper {

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

    public static IClassInfo _findSuperClass(JavaType type) {
        var superClass = type.getSuperClass();
        return superClass != null ? getClassInfo(superClass) : null;
    }

    public static IClassInfo[] _findInterfaces(JavaType type) {
        var interfaces = type.getInterfaces();
        var result = new IClassInfo[interfaces.size()];
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

    /**
     * 寻找 无参构造函数 (不支持成员类)
     *
     * @param classInfo c
     * @return a
     */
    public static IConstructorInfo _findDefaultConstructor(ClassInfo classInfo) {
        for (var constructor : classInfo.constructors()) {
            if (constructor.parameters().length == 0) {
                return constructor;
            }
        }
        return null;
    }

    /**
     * 寻找 Record 规范构造参数
     */
    public static IConstructorInfo _findRecordConstructor(IClassInfo classInfo) {
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

    public static IFieldInfo[] _findAllFieldInfos(IClassInfo classInfo) {
        var allFieldInfos = new ArrayList<IFieldInfo>();
        while (classInfo != null) {
            addAll(allFieldInfos, classInfo.fields());
            classInfo = classInfo.superClass();
        }
        return allFieldInfos.toArray(IFieldInfo[]::new);
    }

    /**
     * 获取当前 ClassInfo 的所有方法 (不包括父类方法 不包括桥接方法)
     *
     * @param classInfo c
     * @return c
     */
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

    public static IMethodInfo[] _findAllMethodInfos(IClassInfo classInfo) {
        //存储所有出现过的父类方法 用于过滤
        var filter = new HashSet<IMethodInfo>();
        var allMethodInfo = new ArrayList<IMethodInfo>();
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
        return allMethodInfo.toArray(IMethodInfo[]::new);
    }

    public static Annotation[] _findAnnotations(Class<?> rawClass) {
        return rawClass.getDeclaredAnnotations();
    }

    /**
     * 获取当前方法的注解 同时包含 重写方法的注解
     *
     * @return a
     */
    public static Annotation[] _findAllAnnotations(IClassInfo classInfo) {
        var allAnnotations = new ArrayList<Annotation>();
        while (classInfo != null) {
            addAll(allAnnotations, classInfo.annotations());
            classInfo = classInfo.superClass();
        }
        return allAnnotations.toArray(Annotation[]::new);
    }

    public static boolean _isFinal(Set<AccessFlag> accessFlags) {
        return accessFlags.contains(AccessFlag.FINAL);
    }

    public static boolean _isStatic(Set<AccessFlag> accessFlags) {
        return accessFlags.contains(AccessFlag.STATIC);
    }

    public static boolean _isAnonymousClass(Class<?> rawClass) {
        return rawClass.isAnonymousClass();
    }

    public static boolean _isMemberClass(Class<?> rawClass) {
        return rawClass.isMemberClass();
    }

    public static boolean _isPrimitive(Class<?> rawClass) {
        return rawClass.isPrimitive();
    }

    public static boolean _isArray(Class<?> rawClass) {
        return rawClass.isArray();
    }

    public static IClassInfo _findEnumClass(ClassInfo classInfo) {
        if (classInfo.classType() == ENUM) {
            return classInfo.isAnonymousClass() ? classInfo.superClass() : classInfo;
        } else {
            return null;
        }
    }

    public static IClassInfo _findComponentType(ClassInfo classInfo) {
        if (classInfo.isArray()) {
            return getClassInfo(classInfo.type().getRawClass().componentType());
        } else {
            return null;
        }
    }

    private static JavaType[] _getRecordComponentsTypes(IClassInfo classInfo) {
        var recordComponents = classInfo.type().getRawClass().getRecordComponents();
        var result = new JavaType[recordComponents.length];
        for (int i = 0; i < recordComponents.length; i = i + 1) {
            result[i] = _findType(recordComponents[i].getGenericType(), classInfo);
        }
        return result;
    }

    private static boolean _hasSameParameterTypes(IConstructorInfo constructorInfo, JavaType[] types) {
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
