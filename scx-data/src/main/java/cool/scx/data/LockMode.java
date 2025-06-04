package cool.scx.data;

public enum LockMode {

    /// 共享锁，允许多个读取，阻止写入
    SHARED,

    /// 排他锁，阻止读取和写入
    EXCLUSIVE

}
