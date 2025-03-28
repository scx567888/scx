package cool.scx.http.headers.accept;

public class IllegalMediaRangeException extends Exception {

    public final String mediaRangeStr;

    public IllegalMediaRangeException(String mediaRangeStr) {
        this.mediaRangeStr = mediaRangeStr;
    }

}
