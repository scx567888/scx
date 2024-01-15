package cool.scx.jdbc.spy.wrapper;

import java.sql.SQLException;
import java.sql.Wrapper;

abstract class AbstractWrapper implements Wrapper {

    private final Wrapper delegate;

    protected AbstractWrapper(Wrapper delegate) {
        this.delegate = delegate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        if (iface.isInstance(delegate)) {
            return (T) delegate;
        }
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return true;
        }
        if (iface.isInstance(delegate)) {
            return true;
        }
        return delegate.isWrapperFor(iface);
    }

}
