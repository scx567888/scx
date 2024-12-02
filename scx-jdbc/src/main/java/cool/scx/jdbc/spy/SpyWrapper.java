package cool.scx.jdbc.spy;

import java.sql.SQLException;
import java.sql.Wrapper;

/**
 * SpyWrapper
 *
 * @author scx567888
 * @version 0.0.1
 */
public class SpyWrapper<W extends Wrapper> implements Wrapper {

    protected final W delegate;
    protected final SpyEventListener eventListener;

    protected SpyWrapper(W delegate, SpyEventListener eventListener) {
        this.delegate = delegate;
        this.eventListener = eventListener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> f) throws SQLException {
        if (f.isInstance(this)) {
            return (T) this;
        }
        if (f.isInstance(delegate)) {
            return (T) delegate;
        }
        return delegate.unwrap(f);
    }

    @Override
    public boolean isWrapperFor(Class<?> f) throws SQLException {
        if (f.isInstance(this)) {
            return true;
        }
        if (f.isInstance(delegate)) {
            return true;
        }
        return delegate.isWrapperFor(f);
    }

}
