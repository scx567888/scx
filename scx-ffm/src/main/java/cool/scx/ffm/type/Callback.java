package cool.scx.ffm.type;

/// 回调函数需要继承此接口, 同时需要创建一个名为 "callback" 的方法
///
/// @author scx567888
/// @version 0.0.1
public interface Callback {

    default String callbackMethodName() {
        return "callback";
    }

}
