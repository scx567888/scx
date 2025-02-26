package cool.scx.reflect;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import static cool.scx.reflect.AccessModifier.PRIVATE;
import static java.util.Collections.addAll;


/// MethodInfoHelper
///
/// @author scx567888
/// @version 0.0.1
final class MethodInfoHelper {

    static String _findName(Method method) {
        return method.getName();
    }

    static boolean _isAbstract(Set<AccessFlag> accessFlags) {
        return accessFlags.contains(AccessFlag.ABSTRACT);
    }

    static boolean _isStatic(Set<AccessFlag> accessFlags) {
        return accessFlags.contains(AccessFlag.STATIC);
    }

    static Annotation[] _findAnnotations(Method method) {
        return method.getDeclaredAnnotations();
    }

    static JavaType _findReturnType(MethodInfo methodInfo) {
        return ReflectHelper._findType(methodInfo.method().getGenericReturnType(), methodInfo.classInfo());
    }

    static ParameterInfo[] _findParameterInfos(MethodInfo methodInfo) {
        var parameters = methodInfo.method().getParameters();
        var result = new ParameterInfo[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            result[i] = new ParameterInfo(parameters[i], methodInfo);
        }
        return result;
    }

    static IMethodInfo _findSuperMethod(MethodInfo methodInfo) {
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
    static Annotation[] _findAllAnnotations(IMethodInfo methodInfo) {
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
    private static boolean isOverride(IMethodInfo rootMethod, IMethodInfo candidateMethod) {
        return PRIVATE != candidateMethod.accessModifier() &&
                candidateMethod.name().equals(rootMethod.name()) &&
                _hasSameParameterTypes(rootMethod, candidateMethod);
    }

    private static boolean _hasSameParameterTypes(IMethodInfo rootMethod, IMethodInfo candidateMethod) {
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

}
