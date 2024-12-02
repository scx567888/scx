package cool.scx.common.lock;

public interface ILockByKey<T> {

    void lock(T key);

    void unlock(T key);

}
