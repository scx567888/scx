package cool.scx.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static cool.scx.reflect.ReflectHelper.*;
import static cool.scx.reflect.ScxReflect.TYPE_INFO_CACHE;
import static java.lang.reflect.AccessFlag.FINAL;
import static java.lang.reflect.AccessFlag.STATIC;

public class ClassInfoImpl implements ClassInfo {

    private final Class<?> rawClass;
    private final GenericInfo[] generics;

    private final String name;
    private final AccessModifier accessModifier;
    private final ClassType classType;
    private final boolean isFinal;
    private final boolean isStatic;
    private final boolean isAnonymousClass;
    private final boolean isMemberClass;
    private final boolean isPrimitive;
    private final boolean isArray;

    private final ClassInfo superClass;
    private final ClassInfo[] interfaces;

    private final ConstructorInfo[] constructors;
    private final ConstructorInfo defaultConstructor;

    private final FieldInfo[] fields;
    private final MethodInfo[] methods;

    private final Annotation[] annotations;

    private final ClassInfo enumClass;
    private final TypeInfo componentType;

    ClassInfoImpl(Type type) {
        //0, 先添加到 CLASS_INFO_CACHE 中
        TYPE_INFO_CACHE.put(type, this);

        // 我们只允许 class 和 parameterizedType 和 GenericArrayType
        switch (type) {
            case Class<?> c -> {
                this.rawClass = c;
                this.generics = new GenericInfo[0];
            }
            case ParameterizedType p -> {
                //这里我们假设 p 一定是 ParameterizedTypeImpl, 所以 getRawType 一定是 class
                this.rawClass = (Class<?>) p.getRawType();
                this.generics = _findGenerics(this.rawClass.getTypeParameters(), p.getActualTypeArguments());
            }
            case GenericArrayType g -> {
                this.rawClass = _findRawClass(g);
                this.generics = new GenericInfo[0];
            }
            default -> {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }

        var accessFlags = this.rawClass.accessFlags();

        this.name = this.rawClass.getName();
        this.accessModifier = _findAccessModifier(accessFlags);
        this.classType = _findClassType(this.rawClass, accessFlags);
        this.isFinal = accessFlags.contains(FINAL);
        this.isStatic = accessFlags.contains(STATIC);
        this.isAnonymousClass = this.rawClass.isAnonymousClass();
        this.isMemberClass = this.rawClass.isMemberClass();
        this.isPrimitive = this.rawClass.isPrimitive();
        this.isArray = this.rawClass.isArray();

        this.superClass = _findSuperClass(this.rawClass);
        this.interfaces = _findInterfaces(this.rawClass);

        this.constructors = _findConstructors(this.rawClass, this);
        this.defaultConstructor = _findDefaultConstructor(this.constructors);

        this.fields = _findFields(this.rawClass, this);
        this.methods = _findMethods(this.rawClass, this);

        this.annotations = this.rawClass.getDeclaredAnnotations();

        this.enumClass = _findEnumClass(this);
        this.componentType = _findComponentType(type);

    }

    @Override
    public Class<?> rawClass() {
        return rawClass;
    }

    @Override
    public GenericInfo[] generics() {
        return generics;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public AccessModifier accessModifier() {
        return accessModifier;
    }

    @Override
    public ClassType classType() {
        return classType;
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
    public boolean isAnonymousClass() {
        return isAnonymousClass;
    }

    @Override
    public boolean isMemberClass() {
        return isMemberClass;
    }

    @Override
    public boolean isPrimitive() {
        return isPrimitive;
    }

    @Override
    public boolean isArray() {
        return isArray;
    }

    @Override
    public ClassInfo superClass() {
        return superClass;
    }

    @Override
    public ClassInfo[] interfaces() {
        return interfaces;
    }

    @Override
    public ConstructorInfo[] constructors() {
        return constructors;
    }

    @Override
    public ConstructorInfo defaultConstructor() {
        return defaultConstructor;
    }

    @Override
    public ConstructorInfo recordConstructor() {
        //todo 待完成
        return null;
    }

    @Override
    public FieldInfo[] fields() {
        return fields;
    }

    @Override
    public FieldInfo[] allFields() {
        //todo 待完成
        return new FieldInfo[0];
    }

    @Override
    public MethodInfo[] methods() {
        return methods;
    }

    @Override
    public MethodInfo[] allMethods() {
        //todo 待完成
        return new MethodInfo[0];
    }

    @Override
    public Annotation[] annotations() {
        return annotations;
    }

    @Override
    public Annotation[] allAnnotations() {
        return new Annotation[0];
    }

    @Override
    public ClassInfo enumClass() {
        return enumClass;
    }

    @Override
    public TypeInfo componentType() {
        return componentType;
    }

}
