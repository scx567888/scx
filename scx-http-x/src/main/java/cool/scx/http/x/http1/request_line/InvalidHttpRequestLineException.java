package cool.scx.http.x.http1.request_line;

/// 非法请求行异常
public class InvalidHttpRequestLineException extends Exception {

    public final String requestLineStr;

    public InvalidHttpRequestLineException(String requestLineStr) {
        this.requestLineStr = requestLineStr;
    }

}
