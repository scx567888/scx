package cool.scx.http.body;

/// 请求体已经被消费异常
public class BodyAlreadyConsumedException extends IllegalStateException {

    public BodyAlreadyConsumedException() {
        super("The body has already been consumed.");
    }

}
