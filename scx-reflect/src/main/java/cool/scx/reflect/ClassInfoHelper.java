package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;

import static cool.scx.reflect.ReflectHelper._findType;
import static cool.scx.reflect.ReflectHelper.getClassInfo;
import static java.util.Collections.addAll;


/**
 * ClassInfoHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
class ClassInfoHelper {

    //********************* ClassInfo START *********************

    static ClassInfo _findSuperClass(ClassInfo classInfo) {
        return classInfo.type().getSuperClass() != null ? getClassInfo(classInfo.type().getSuperClass()) : null;
    }

    static ClassInfo[] _findInterfaces(ClassInfo classInfo) {
        var interfaces = classInfo.type().getInterfaces();
        var result = new ClassInfo[interfaces.size()];
        for (int i = 0; i < interfaces.size(); i = i + 1) {
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


}
