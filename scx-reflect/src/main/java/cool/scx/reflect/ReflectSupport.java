package cool.scx.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

import static cool.scx.reflect.AccessModifier.PRIVATE;
import static cool.scx.reflect.ClassKind.ENUM;
import static cool.scx.reflect.ClassKind.RECORD;
import static java.util.Collections.addAll;

/// 内部构建帮助类
final class ReflectSupport {

    public static TypeInfo _findComponentType(Type type, Map<TypeVariable<?>, TypeInfo> bindings) {
        // 我们假设 此处 type 已经被正确过滤了 所以不做过多判断了
        var componentType = switch (type) {
            case Class<?> c -> c.componentType();
            case GenericArrayType g -> g.getGenericComponentType();
            default -> throw new IllegalArgumentException("unsupported type: " + type);
        };
        return ScxReflect.getType(componentType, bindings);
    }

    public static Class<?> _findRawClass(Type type) {
        // 我们假设 此处 type 已经被正确过滤了 所以不做过多判断了
        return switch (type) {
            case Class<?> c -> c;
            // 我们假设 ParameterizedType 不是用户自定义的 那么 getRawType 的返回值实际上永远都是 Class
            case ParameterizedType p -> (Class<?>) p.getRawType();
            default -> throw new IllegalArgumentException("unsupported type: " + type);
        };
    }

    public static Map<TypeVariable<?>, TypeInfo> _findBindings(Type type, Map<TypeVariable<?>, TypeInfo> bindings) {
        // 我们假设 此处 type 已经被正确过滤了 所以不做过多判断了
        switch (type) {
            // Class 没有 bindings 的概念
            case Class<?> _ -> {
                return Map.of();
            }
            // 我们假设 ParameterizedType 不是用户自定义的 那么 getRawType 的返回值实际上永远都是 Class
            case ParameterizedType p -> {
                //这里我们假设 typeParameters 和 actualTypeArguments 的长度和顺序是完全一一对应的
                var typeParameters = ((Class<?>) p.getRawType()).getTypeParameters();
                var actualTypeArguments = p.getActualTypeArguments();

                var result = new LinkedHashMap<TypeVariable<?>, TypeInfo>();

                for (int i = 0; i < typeParameters.length; i = i + 1) {
                    var typeParameter = typeParameters[i];
                    var actualTypeArgument = actualTypeArguments[i];
                    var typeInfo = ScxReflect.getType(actualTypeArgument, bindings);
                    result.put(typeParameter, typeInfo);
                }

                return result;
            }
            default -> throw new IllegalArgumentException("unsupported type: " + type);
        }
    }

    public static AccessModifier _findAccessModifier(Set<AccessFlag> accessFlags) {
        if (accessFlags.contains(AccessFlag.PUBLIC)) {
            return AccessModifier.PUBLIC;
        }
        if (accessFlags.contains(AccessFlag.PROTECTED)) {
            return AccessModifier.PROTECTED;
        }
        if (accessFlags.contains(AccessFlag.PRIVATE)) {
            return PRIVATE;
        }
        return AccessModifier.PACKAGE_PRIVATE;
    }

    public static ClassKind _findClassKind(Class<?> rawClass, Set<AccessFlag> accessFlags) {
        if (accessFlags.contains(AccessFlag.ANNOTATION)) {
            return ClassKind.ANNOTATION;
        }
        if (accessFlags.contains(AccessFlag.INTERFACE)) {
            return ClassKind.INTERFACE;
        }
        if (accessFlags.contains(AccessFlag.ENUM)) {
            return ENUM;
        }
        if (rawClass.isRecord()) {
            return RECORD;
        }
        return ClassKind.CLASS;
    }

    public static ClassInfo _findSuperClass(Class<?> rawClass, Map<TypeVariable<?>, TypeInfo> bindings) {
        var superClass = rawClass.getGenericSuperclass();
        // superClass 只可能是 Class (非数组,非基本类型) 或 ParameterizedType (rawClass 同样非数组,非基本类型)
        // 所以我们 使用 getType 返回的也必然是 ClassInfo, 此处强转安全
        return superClass != null ? (ClassInfo) ScxReflect.getType(superClass, bindings) : null;
    }

    public static ClassInfo[] _findInterfaces(Class<?> rawClass, Map<TypeVariable<?>, TypeInfo> bindings) {
        var interfaces = rawClass.getGenericInterfaces();
        // interface 只可能是 Class (非数组,非基本类型) 或 ParameterizedType (rawClass 同样非数组,非基本类型)
        // 所以我们 使用 getType 返回的也必然是 ClassInfo, 此处强转安全
        var result = new ClassInfo[interfaces.length];
        for (int i = 0; i < interfaces.length; i = i + 1) {
            result[i] = (ClassInfo) ScxReflect.getType(interfaces[i], bindings);
        }
        return result;
    }

    public static ConstructorInfo[] _findConstructors(Class<?> rawClass, ClassInfo classInfo) {
        var constructors = rawClass.getDeclaredConstructors();
        var result = new ConstructorInfo[constructors.length];
        for (int i = 0; i < constructors.length; i = i + 1) {
            result[i] = new ConstructorInfoImpl(constructors[i], classInfo);
        }
        return result;
    }

    public static FieldInfo[] _findFields(Class<?> rawClass, ClassInfo classInfo) {
        var fields = rawClass.getDeclaredFields();
        var result = new FieldInfo[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            result[i] = new FieldInfoImpl(fields[i], classInfo);
        }
        return result;
    }

    /// 获取当前 ClassInfo 的所有方法 (不包括桥接方法)
    public static MethodInfo[] _findMethods(Class<?> rawClass, ClassInfo classInfo) {
        var list = new ArrayList<MethodInfo>();
        var methods = rawClass.getDeclaredMethods();
        for (var method : methods) {
            //过滤掉桥接方法, 因为我们几乎用不到
            if (!method.isBridge()) {
                list.add(new MethodInfoImpl(method, classInfo));
            }
        }
        return list.toArray(MethodInfo[]::new);
    }

    public static ParameterInfo[] _findParameters(Executable rawExecutable, ExecutableInfo executableInfo) {
        var parameters = rawExecutable.getParameters();
        var result = new ParameterInfo[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            result[i] = new ParameterInfoImpl(parameters[i], executableInfo);
        }
        return result;
    }

    /// 寻找 无参构造函数 (不支持成员类)
    public static ConstructorInfo _findDefaultConstructor(ConstructorInfo[] constructors) {
        for (var constructor : constructors) {
            if (constructor.parameters().length == 0) {
                return constructor;
            }
        }
        return null;
    }

    /// 寻找 Record 规范构造参数
    public static ConstructorInfo _findRecordConstructor(ClassInfo classInfo) {
        if (classInfo.classKind() != RECORD) {
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

    private static TypeInfo[] _getRecordComponentsTypes(ClassInfo classInfo) {
        var recordComponents = classInfo.rawClass().getRecordComponents();
        var result = new TypeInfo[recordComponents.length];
        for (int i = 0; i < recordComponents.length; i = i + 1) {
            result[i] = ScxReflect.getType(recordComponents[i].getGenericType(), classInfo.bindings());
        }
        return result;
    }

    private static boolean _hasSameParameterTypes(ExecutableInfo constructorInfo, TypeInfo[] types) {
        if (constructorInfo.parameters().length != types.length) {
            return false;
        }
        var p1 = constructorInfo.parameters();
        for (int i = 0; i < p1.length; i = i + 1) {
            var p1Type = p1[i].parameterType();
            var p2Type = types[i];
            if (p1Type != p2Type) {
                return false;
            }
        }
        return true;
    }

    public static FieldInfo[] _findAllFields(ClassInfo classInfo) {
        var allFieldInfos = new ArrayList<FieldInfo>();
        while (classInfo != null) {
            addAll(allFieldInfos, classInfo.fields());
            classInfo = classInfo.superClass();
        }
        return allFieldInfos.toArray(FieldInfo[]::new);
    }

    public static MethodInfo[] _findAllMethods(ClassInfo classInfo) {
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

    /// 返回当前 ClassInfo 所表示的枚举类的“真实”枚举类型。
    /// 在 Java 中，枚举常量有时会被编译成枚举的匿名子类（匿名枚举类），
    /// 这时直接拿匿名类的 ClassInfo 并不能代表真正的枚举类型。
    /// 该方法的作用是：
    /// - 如果 classInfo 表示的是匿名枚举类，则返回它的父类（即真正的枚举类）。
    /// - 如果 classInfo 是普通的枚举类，则直接返回它自身。
    /// - 如果不是枚举类，则返回 null。
    /// 这样在处理枚举类型时，可以统一拿到“真实”的枚举声明类，方便后续处理。
    ///
    /// @param classInfo 需要判断的 ClassInfo 对象
    /// @return 真实的枚举类型 ClassInfo，或者非枚举时返回 null
    public static ClassInfo _findEnumClass(ClassInfo classInfo) {
        if (classInfo.classKind() == ENUM) {
            return classInfo.isAnonymousClass() ? classInfo.superClass() : classInfo;
        } else {
            return null;
        }
    }

    public static MethodInfo _findSuperMethod(MethodInfo methodInfo) {
        var superClass = methodInfo.declaringClass().superClass();
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

    /// 判断是否为重写方法
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
            //todo 这个判断合理吗？ 
            var p1Type = p1[i].parameterType();
            var p2Type = p2[i].parameterType();
            if (p1Type != p2Type) {
                return false;
            }
        }
        return true;
    }

    /// 获取当前方法的注解 同时包含 重写方法的注解
    public static Annotation[] _findAllAnnotations(ClassInfo classInfo) {
        var allAnnotations = new ArrayList<Annotation>();
        while (classInfo != null) {
            addAll(allAnnotations, classInfo.annotations());
            classInfo = classInfo.superClass();
        }
        return allAnnotations.toArray(Annotation[]::new);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

   

    


    

   

  

   

 

   



    

   

  

    

    

    



    


   


//
    
//


//
//    /// 获取当前方法的注解 同时包含 重写方法的注解
//    ///
//    /// @return a
//    public static Annotation[] _findAllAnnotations(MethodInfo methodInfo) {
//        var allAnnotations = new ArrayList<Annotation>();
//        while (methodInfo != null) {
//            addAll(allAnnotations, methodInfo.annotations());
//            methodInfo = methodInfo.superMethod();
//        }
//        return allAnnotations.toArray(Annotation[]::new);
//    }

}
