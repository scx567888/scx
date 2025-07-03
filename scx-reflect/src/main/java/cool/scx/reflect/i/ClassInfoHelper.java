package cool.scx.reflect.i;

import java.lang.reflect.AccessFlag;
import java.util.Set;

import static cool.scx.reflect.i.ClassType.ENUM;
import static cool.scx.reflect.i.ClassType.RECORD;

public class ClassInfoHelper {

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

    public static ClassInfo _findSuperClass(Class<?> rawClass) {
        var superClass = rawClass.getGenericSuperclass();
        return superClass != null ? ScxReflect.getClassInfo(superClass) : null;
    }

    public static ClassInfo[] _findInterfaces(Class<?> rawClass) {
        var interfaces = rawClass.getGenericInterfaces();
        var result = new ClassInfo[interfaces.length];
        for (int i = 0; i < interfaces.length; i = i + 1) {
            result[i] = ScxReflect.getClassInfo(interfaces[i]);
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

    /// 寻找 无参构造函数 (不支持成员类)
    public static ConstructorInfo _findDefaultConstructor(ConstructorInfo[] constructors) {
        for (var constructor : constructors) {
            if (constructor.parameters().length == 0) {
                return constructor;
            }
        }
        return null;
    }

    public static FieldInfo[] _findFields(Class<?> rawClass, ClassInfo classInfo) {
        var fields = rawClass.getDeclaredFields();
        var result = new FieldInfo[fields.length];
        for (int i = 0; i < fields.length; i = i + 1) {
            result[i] = new FieldInfoImpl(fields[i], classInfo);
        }
        return result;
    }

    /// 获取当前 ClassInfo 的所有方法 (不包括父类方法 不包括桥接方法)
    public static MethodInfo[] _findMethods(Class<?> rawClass, ClassInfo classInfo) {
        var methods = rawClass.getDeclaredMethods();
        var result = new MethodInfo[methods.length];
        for (int i = 0; i < methods.length; i = i + 1) {
            result[i] = new MethodInfoImpl(methods[i], classInfo);
        }
        return result;
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
        if (classInfo.classType() == ENUM) {
            return classInfo.isAnonymousClass() ? classInfo.superClass() : classInfo;
        } else {
            return null;
        }
    }

    public static ClassInfo _findComponentType(ClassInfo classInfo) {
        if (classInfo.isArray()) {
            return ScxReflect.getClassInfo(classInfo.rawClass().componentType());
        } else {
            return null;
        }
    }

}
