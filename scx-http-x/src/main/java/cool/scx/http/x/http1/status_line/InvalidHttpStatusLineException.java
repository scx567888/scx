package cool.scx.http.x.http1.status_line;

/// 非法响应行异常
public class InvalidHttpStatusLineException extends Exception {

    public final String statusLineStr;

    public InvalidHttpStatusLineException(String statusLineStr) {
        this.statusLineStr = statusLineStr;
    }

}
