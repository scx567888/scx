package cool.scx.core.eventbus;

public record ZeroCopyMessageWrapper<T>(T message) {

    public static <T> ZeroCopyMessageWrapper<T> zeroCopyMessage(T message) {
        return new ZeroCopyMessageWrapper<>(message);
    }

}
