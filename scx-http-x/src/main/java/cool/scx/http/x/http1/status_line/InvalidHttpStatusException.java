package cool.scx.http.x.http1.status_line;

/// 非法 Http 响应状态
public class InvalidHttpStatusException extends Exception {

    public final String statusStr;

    public InvalidHttpStatusException(String statusStr) {
        this.statusStr = statusStr;
    }

}
