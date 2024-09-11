package cool.scx.http;

//todo 方法定义待优化
public interface ScxHttpResponse {

    HttpStatusCode status();

    ScxHttpHeadersWritable headers();

    ScxHttpResponse status(HttpStatusCode code);

    void send(byte[] data);

    void send(String data);

    void send();

    boolean closed();

}
