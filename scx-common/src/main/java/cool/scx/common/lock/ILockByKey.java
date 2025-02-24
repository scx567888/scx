package cool.scx.common.lock;

/// LockByKey
public interface ILockByKey<T> {

    void lock(T key);

    void unlock(T key);

}
