package cool.scx.http_server;

public enum ScxHttpHeaderNameEnum implements ScxHttpHeaderName {
    
    CONTENT_TYPE;

    public static ScxHttpHeaderNameEnum of(String s) {
        String lowerCase = s.toLowerCase();
        if (lowerCase.equals("content-type")) {
            return CONTENT_TYPE;
        }
        throw new IllegalArgumentException("Not Found ScxHttpHeaderNameEnum : " + s);
    }
    
}
