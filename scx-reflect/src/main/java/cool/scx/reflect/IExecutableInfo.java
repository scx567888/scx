package cool.scx.reflect;

/// 表示 可执行的 比如 普通方法或构造方法
///
/// @author scx567888
/// @version 0.0.1
public interface IExecutableInfo {

    ParameterInfo[] parameters();

    ClassInfo classInfo();

}
