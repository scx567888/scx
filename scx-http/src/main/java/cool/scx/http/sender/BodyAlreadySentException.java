package cool.scx.http.sender;

/// 内容已经发送过异常
public class BodyAlreadySentException extends IllegalStateException {
    
    public BodyAlreadySentException() {
        super("The body has already been sent.");
    }

}
