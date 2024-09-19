package cool.scx.common.ffm.type;

/**
 * 回调函数需要继承此接口
 */
public interface Callback {

    default String callbackMethodName() {
        return "callback";
    }

}
