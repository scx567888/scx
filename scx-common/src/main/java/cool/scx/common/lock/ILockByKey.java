package cool.scx.common.lock;

/// ILockByKey
///
/// @author scx567888
/// @version 0.0.1
public interface ILockByKey<T> {

    void lock(T key);

    void unlock(T key);

}
