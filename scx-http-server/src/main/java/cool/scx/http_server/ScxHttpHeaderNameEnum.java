package cool.scx.http_server;

public enum ScxHttpHeaderNameEnum implements ScxHttpHeaderName {
    
    CONTENT_TYPE;

    public static ScxHttpHeaderNameEnum of(String s) {
        throw new IllegalArgumentException("Not Found ScxHttpHeaderNameEnum : " + s);
    }
    
}
