package cool.scx.http.media_type;

public class IllegalMediaTypeException extends Exception{
    
    public final String mediaTypeStr;

    public IllegalMediaTypeException(String mediaTypeStr) {
        this.mediaTypeStr = mediaTypeStr;
    }
    
}
