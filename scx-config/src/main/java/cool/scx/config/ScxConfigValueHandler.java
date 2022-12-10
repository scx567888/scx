package cool.scx.config;

/**
 * <p>ScxConfigValueHandler interface.</p>
 *
 * @author scx567888
 * @version 0.0.1
 */
@FunctionalInterface
public interface ScxConfigValueHandler<T> {

    /**
     * <p>handle.</p>
     *
     * @param keyPath  a {@link java.lang.String} object
     * @param rawValue a {@link java.lang.Object} object
     * @return a T object
     */
    T handle(String keyPath, Object rawValue);

}
