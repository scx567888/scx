package cool.scx.reflect.i;

import java.lang.reflect.Method;

public interface MethodInfo extends ExecutableInfo, MemberInfo, AnnotatedElementInfo {

    Method rawMethod();

    String name();

    ClassInfo returnType();

}
