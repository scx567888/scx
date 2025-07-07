package cool.scx.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import static cool.scx.reflect.ReflectSupport._findAccessModifier;
import static cool.scx.reflect.ReflectSupport._findParameters;

/// 构造函数 信息
///
/// @author scx567888
/// @version 0.0.1
final class ConstructorInfoImpl implements ConstructorInfo {

    private final Constructor<?> rawConstructor;
    private final ClassInfo declaringClass;
    private final AccessModifier accessModifier;
    private final ParameterInfo[] parameters;
    private final Annotation[] annotations;

    ConstructorInfoImpl(Constructor<?> constructor, ClassInfo declaringClass) {
        this.rawConstructor = constructor;
        this.declaringClass = declaringClass;
        var accessFlags = this.rawConstructor.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.parameters = _findParameters(this.rawConstructor, this);
        this.annotations = this.rawConstructor.getDeclaredAnnotations();
    }

    @Override
    public Constructor<?> rawConstructor() {
        return rawConstructor;
    }

    @Override
    public ClassInfo declaringClass() {
        return declaringClass;
    }

    @Override
    public AccessModifier accessModifier() {
        return accessModifier;
    }

    @Override
    public ParameterInfo[] parameters() {
        return parameters;
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();

        // 修饰符
        sb.append(accessModifier.name().toLowerCase());

        // 名称（类名）
        sb.append(" ");
        sb.append(declaringClass.rawClass().getSimpleName());

        // 参数列表
        sb.append("(");
        if (parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                var param = parameters[i];
                sb.append(param.parameterType());
                sb.append(" ");
                sb.append(param.name());
                if (i < parameters.length - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");

        return sb.toString();
    }


}
