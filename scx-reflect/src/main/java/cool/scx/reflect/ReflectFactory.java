package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static cool.scx.common.util.ObjectUtils.constructType;
import static cool.scx.common.util.ObjectUtils.resolveMemberType;
import static cool.scx.reflect.AccessModifier.*;
import static java.util.Collections.addAll;

public final class ReflectFactory {

    private static final Map<JavaType, ClassInfo> CLASS_INFO_CACHE = new HashMap<>();

    public static ClassInfo getClassInfo(Class<?> javaType) {
        return getClassInfo(constructType(javaType));
    }

    public static ClassInfo getClassInfo(JavaType javaType) {
        var classInfo = CLASS_INFO_CACHE.get(javaType);
        if (classInfo == null) {
            classInfo = new ClassInfo(javaType);
            CLASS_INFO_CACHE.put(javaType, classInfo);
        }
        return classInfo;
    }

    private static AccessModifier _findAccessModifier(int m) {
        if (Modifier.isPublic(m)) {
            return PUBLIC;
        } else if (Modifier.isProtected(m)) {
            return PROTECTED;
        } else if (Modifier.isPrivate(m)) {
            return PRIVATE;
        } else {
            return DEFAULT;
        }
    }

    private static JavaType _findType(Type type, ClassInfo classInfo) {
        return resolveMemberType(type, classInfo.type().getBindings());
    }

    //********************* ClassInfo START *********************

    static ClassInfo _findSuperClass(ClassInfo classInfo) {
        return classInfo.type().getSuperClass() != null ? getClassInfo(classInfo.type().getSuperClass()) : null;
    }

    static ClassInfo[] _findInterfaces(ClassInfo classInfo) {
        var interfaces = classInfo.type().getInterfaces();
        var result = new ClassInfo[interfaces.size()];
        for (int i = 0; i < interfaces.size(); i++) {
            result[i] = getClassInfo(interfaces.get(i));
        }
        return result;
    }

    static boolean _isRecord(ClassInfo classInfo) {
        return classInfo.type().getRawClass().isRecord();
    }

    static boolean _isInterface(ClassInfo classInfo) {
        return classInfo.type().getRawClass().isInterface();
    }

    static boolean _isAbstract(ClassInfo classInfo) {
        return Modifier.isAbstract(classInfo.type().getRawClass().getModifiers());
    }

    /**
     * 判断是否为 Enum , {@link Class#isEnum()} 无法处理内部类的情况
     *
     * @param classInfo classInfo
     * @return isEnum
     */
    static boolean _isEnum(ClassInfo classInfo) {
        return Enum.class.isAssignableFrom(classInfo.type().getRawClass());
    }

    static boolean _isAnonymousClass(ClassInfo classInfo) {
        return classInfo.type().getRawClass().isAnonymousClass();
    }

    static Annotation[] _findAnnotations(ClassInfo classInfo) {
        return classInfo.type().getRawClass().getDeclaredAnnotations();
    }

    static ConstructorInfo[] _findConstructorInfos(ClassInfo classInfo) {
        var constructors = classInfo.type().getRawClass().getDeclaredConstructors();
        var result = new ConstructorInfo[constructors.length];
        for (int i = 0; i < constructors.length; i = i + 1) {
            result[i] = new ConstructorInfo(constructors[i], classInfo);
        }
        return result;
    }

    static FieldInfo[] _findFieldInfos(ClassInfo classInfo) {
        var fields = classInfo.type().getRawClass().getDeclaredFields();
        var result = new FieldInfo[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            result[i] = new FieldInfo(fields[i], classInfo);
        }
        return result;
    }

    /**
     * 获取当前 ClassInfo 的所有方法 (不包括父类方法 不包括桥接方法)
     *
     * @param classInfo c
     * @return c
     */
    static MethodInfo[] _findMethodInfos(ClassInfo classInfo) {
        var methods = classInfo.type().getRawClass().getDeclaredMethods();
        var list = new ArrayList<MethodInfo>();
        for (var method : methods) {
            if (!method.isBridge()) {
                list.add(new MethodInfo(method, classInfo));
            }
        }
        return list.toArray(MethodInfo[]::new);
    }

    /**
     * 获取当前方法的注解 同时包含 重写方法的注解
     *
     * @return a
     */
    static Annotation[] _findAllAnnotations(ClassInfo classInfo) {
        var allAnnotations = new ArrayList<Annotation>();
        while (classInfo != null) {
            addAll(allAnnotations, classInfo.annotations());
            classInfo = classInfo.superClass();
        }
        return allAnnotations.toArray(Annotation[]::new);
    }

    static FieldInfo[] _findAllFieldInfos(ClassInfo classInfo) {
        var allFieldInfos = new ArrayList<FieldInfo>();
        while (classInfo != null) {
            addAll(allFieldInfos, classInfo.fields());
            classInfo = classInfo.superClass();
        }
        return allFieldInfos.toArray(FieldInfo[]::new);
    }

    static MethodInfo[] _findAllMethodInfos(ClassInfo classInfo) {
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

    /**
     * 寻找 无参构造函数 (不支持成员类)
     *
     * @param classInfo c
     * @return a
     */
    static ConstructorInfo _findNoArgsConstructor(ClassInfo classInfo) {
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
    static ConstructorInfo _findRecordConstructor(ClassInfo classInfo) {
        if (!classInfo.isRecord()) {
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

    static ClassInfo _findEnumClass(ClassInfo classInfo) {
        if (classInfo.isEnum()) {
            return classInfo.isAnonymousClass() ? classInfo.superClass() : classInfo;
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

    private static boolean _hasSameParameterTypes(ConstructorInfo constructorInfo, JavaType[] types) {
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

    //********************* ClassInfo END *********************

    //*********************** ConstructorInfo START *****************

    static AccessModifier _findAccessModifier(ConstructorInfo constructorInfo) {
        return _findAccessModifier(constructorInfo.constructor().getModifiers());
    }

    static ParameterInfo[] _findParameterInfos(ConstructorInfo constructorInfo) {
        var parameters = constructorInfo.constructor().getParameters();
        var result = new ParameterInfo[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            result[i] = new ParameterInfo(parameters[i], constructorInfo);
        }
        return result;
    }

    //*********************** ConstructorInfo END *****************

    //************************* ParameterInfo START **************

    static String _findName(ParameterInfo parameterInfo) {
        return parameterInfo.parameter().getName();
    }

    static JavaType _findType(ParameterInfo parameterInfo) {
        return _findType(parameterInfo.parameter().getParameterizedType(), parameterInfo.executableInfo().classInfo());
    }

    //************************* ParameterInfo END **************

    //*********************** FieldInfo START ********************

    static String _findName(FieldInfo fieldInfo) {
        return fieldInfo.field().getName();
    }

    static AccessModifier _findAccessModifier(FieldInfo fieldInfo) {
        return _findAccessModifier(fieldInfo.field().getModifiers());
    }

    static JavaType _findType(FieldInfo fieldInfo) {
        return _findType(fieldInfo.field().getGenericType(), fieldInfo.classInfo());
    }

    static Annotation[] _findAnnotations(FieldInfo fieldInfo) {
        return fieldInfo.field().getDeclaredAnnotations();
    }

    //*********************** FieldInfo END *******************

    //*********************** MethodInfo START *******************

    static String _findName(MethodInfo methodInfo) {
        return methodInfo.method().getName();
    }

    static boolean _isAbstract(MethodInfo methodInfo) {
        return Modifier.isAbstract(methodInfo.method().getModifiers());
    }

    static boolean _isStatic(MethodInfo methodInfo) {
        return Modifier.isStatic(methodInfo.method().getModifiers());
    }

    static AccessModifier _findAccessModifier(MethodInfo methodInfo) {
        return _findAccessModifier(methodInfo.method().getModifiers());
    }

    static Annotation[] _findAnnotations(MethodInfo methodInfo) {
        return methodInfo.method().getDeclaredAnnotations();
    }

    static JavaType _findReturnType(MethodInfo methodInfo) {
        return _findType(methodInfo.method().getGenericReturnType(), methodInfo.classInfo());
    }

    static ParameterInfo[] _findParameterInfos(MethodInfo methodInfo) {
        var parameters = methodInfo.method().getParameters();
        var result = new ParameterInfo[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            result[i] = new ParameterInfo(parameters[i], methodInfo);
        }
        return result;
    }

    static MethodInfo _findSuperMethod(MethodInfo methodInfo) {
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

    /**
     * 获取当前方法的注解 同时包含 重写方法的注解
     *
     * @return a
     */
    static Annotation[] _findAllAnnotations(MethodInfo methodInfo) {
        var allAnnotations = new ArrayList<Annotation>();
        while (methodInfo != null) {
            addAll(allAnnotations, methodInfo.annotations());
            methodInfo = methodInfo.superMethod();
        }
        return allAnnotations.toArray(Annotation[]::new);
    }


    /**
     * 判断是否为重写方法
     *
     * @param rootMethod      a
     * @param candidateMethod a
     * @return a
     */
    private static boolean isOverride(MethodInfo rootMethod, MethodInfo candidateMethod) {
        return PRIVATE != candidateMethod.accessModifier() &&
               candidateMethod.name().equals(rootMethod.name()) &&
               _hasSameParameterTypes(rootMethod, candidateMethod);
    }

    private static boolean _hasSameParameterTypes(MethodInfo rootMethod, MethodInfo candidateMethod) {
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

    //*********************** MethodInfo END *******************

}
