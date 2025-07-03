package cool.scx.reflect.i;

import java.lang.reflect.Constructor;

public class ConstructorInfoHelper {

    public static ParameterInfo[] _findParameters(Constructor<?> rawConstructor, ConstructorInfo constructorInfo) {
        var parameters = rawConstructor.getParameters();
        var result = new ParameterInfo[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            result[i] = new ParameterInfoImpl(parameters[i], constructorInfo);
        }
        return result;
    }
    
}
