package cool.scx.data;

import cool.scx.data.exception.DataAccessException;

public interface TransactionContext extends DataContext {

    /// 提交事务
    void commit() throws DataAccessException;

    /// 回滚事务
    void rollback() throws DataAccessException;

}
