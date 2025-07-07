package cool.scx.reflect.impl;

import cool.scx.reflect.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Method;

import static cool.scx.reflect.impl.ReflectHelper.*;

public final class MethodInfoImpl implements MethodInfo {

    private final Method rawMethod;
    private final ClassInfo declaringClass;
    private final String name;
    private final AccessModifier accessModifier;
    private final boolean isAbstract;
    private final boolean isFinal;
    private final boolean isStatic;
    private final boolean isNative;
    private final boolean isDefault;
    private final ParameterInfo[] parameters;
    private final TypeInfo returnType;
    private final MethodInfo superMethod;
    private final Annotation[] annotations;

    MethodInfoImpl(Method method, ClassInfo declaringClass) {
        this.rawMethod = method;
        this.declaringClass = declaringClass;
        this.name = this.rawMethod.getName();
        var accessFlags = this.rawMethod.accessFlags();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.isAbstract = accessFlags.contains(AccessFlag.ABSTRACT);
        this.isFinal = accessFlags.contains(AccessFlag.FINAL);
        this.isStatic = accessFlags.contains(AccessFlag.STATIC);
        this.isNative = accessFlags.contains(AccessFlag.NATIVE);
        this.isDefault = this.rawMethod.isDefault();
        this.parameters = _findParameters(this.rawMethod, this);
        this.returnType = TypeFactory.getType(this.rawMethod.getGenericReturnType(), this.declaringClass.bindings());
        this.superMethod = _findSuperMethod(this);
        this.annotations = this.rawMethod.getDeclaredAnnotations();
    }

    @Override
    public Method rawMethod() {
        return rawMethod;
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
    public String name() {
        return name;
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    @Override
    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public boolean isNative() {
        return isNative;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public ParameterInfo[] parameters() {
        return parameters;
    }

    @Override
    public TypeInfo returnType() {
        return returnType;
    }

    @Override
    public MethodInfo superMethod() {
        return superMethod;
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

        if (isStatic) {
            sb.append(" static");
        }
        if (isAbstract) {
            sb.append(" abstract");
        }
        if (isFinal) {
            sb.append(" final");
        }

        // 返回类型
        sb.append(" ").append(returnType);

        // 类名 + 方法名
        sb.append(" ").append(declaringClass.name()).append(".").append(name);

        // 参数列表
        sb.append("(");
        for (int i = 0; i < parameters.length; i++) {
            var p = parameters[i];
            sb.append(p.parameterType());
            sb.append(" ");
            sb.append(p.name());
            // 也可以加上 p.name()，如果你需要显示参数名（有些编译时没有保留）
            if (i < parameters.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");

        return sb.toString();
    }


}
