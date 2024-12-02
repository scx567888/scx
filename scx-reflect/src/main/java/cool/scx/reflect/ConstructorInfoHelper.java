package cool.scx.reflect;

/**
 * ConstructorInfoHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
class ConstructorInfoHelper {

    static AccessModifier _findAccessModifier(ConstructorInfo constructorInfo) {
        return ReflectHelper._findAccessModifier(constructorInfo.constructor().getModifiers());
    }

    static ParameterInfo[] _findParameterInfos(ConstructorInfo constructorInfo) {
        var parameters = constructorInfo.constructor().getParameters();
        var result = new ParameterInfo[parameters.length];
        for (int i = 0; i < parameters.length; i = i + 1) {
            result[i] = new ParameterInfo(parameters[i], constructorInfo);
        }
        return result;
    }

    //*********************** ConstructorInfo END *****************

}
