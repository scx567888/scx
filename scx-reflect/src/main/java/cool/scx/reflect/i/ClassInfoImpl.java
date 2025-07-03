package cool.scx.reflect.i;

import cool.scx.reflect.MethodInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static cool.scx.reflect.i.ScxReflect.CLASS_INFO_CACHE;
import static cool.scx.reflect.i.ScxReflect.getClassInfo;

public class ClassInfoImpl implements ClassInfo {

    private Class<?> rawClass;
    private String name;

    public ClassInfoImpl(Type type) {
        //0, 先添加到 CLASS_INFO_CACHE 中
        CLASS_INFO_CACHE.put(type, this);
        if (type instanceof Class<?> c) {
            rawClass = c;
        } else if (type instanceof ParameterizedType t) {
            Type rawType = t.getRawType();
            Type[] actualTypeArguments = t.getActualTypeArguments();
            if (rawType instanceof Class<?> c) {
                rawClass = c;
            }
        }
        name= type.getTypeName();
        Field[] declaredFields = rawClass.getDeclaredFields();
        for (Field field : declaredFields) {
            Class<?> type1 = field.getType();
            ClassInfo classInfo = getClassInfo(type1);
//            this.addddd.add(classInfo);
        }
    }

    @Override
    public Class<?> rawClass() {
        return rawClass;
    }

    @Override
    public String name() {
        return "";
    }

    @Override
    public AccessModifier accessModifier() {
        return null;
    }

    @Override
    public ClassType classType() {
        return null;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isAnonymousClass() {
        return false;
    }

    @Override
    public boolean isMemberClass() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public ClassInfo superClass() {
        return null;
    }

    @Override
    public ClassInfo[] interfaces() {
        return new ClassInfo[0];
    }

    @Override
    public ConstructorInfo[] constructors() {
        return new ConstructorInfo[0];
    }

    @Override
    public ConstructorInfo defaultConstructor() {
        return null;
    }

    @Override
    public ConstructorInfo recordConstructor() {
        return null;
    }

    @Override
    public FieldInfo[] fields() {
        return new FieldInfo[0];
    }

    @Override
    public FieldInfo[] allFields() {
        return new FieldInfo[0];
    }

    @Override
    public MethodInfo[] methods() {
        return new MethodInfo[0];
    }

    @Override
    public MethodInfo[] allMethods() {
        return new MethodInfo[0];
    }

    @Override
    public Annotation[] annotations() {
        return new Annotation[0];
    }

    @Override
    public Annotation[] allAnnotations() {
        return new Annotation[0];
    }

    @Override
    public ClassInfo enumClass() {
        return null;
    }

    @Override
    public ClassInfo componentType() {
        return null;
    }

    @Override
    public ClassInfo[] generics() {
        return new ClassInfo[0];
    }

}
