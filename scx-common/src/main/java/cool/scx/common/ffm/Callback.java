package cool.scx.common.ffm;

/**
 * 回调函数需要继承此接口, 同时需要创建一个名为 "callback" 的方法
 */
public interface Callback {

    default String callbackMethodName() {
        return "callback";
    }

}
