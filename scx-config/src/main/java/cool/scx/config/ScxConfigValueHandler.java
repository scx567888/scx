package cool.scx.config;


@FunctionalInterface
public interface ScxConfigValueHandler<T> {

    T handle(String keyPath, Object rawValue);

}
