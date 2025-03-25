package cool.scx.http.x.http1.request_line;

/// 非法 Http 版本
public class InvalidHttpVersion extends Exception {

    public final String versionStr;

    public InvalidHttpVersion(String versionStr) {
        this.versionStr = versionStr;
    }

}
