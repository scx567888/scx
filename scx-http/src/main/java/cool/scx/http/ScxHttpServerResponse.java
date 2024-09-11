package cool.scx.http;

//todo 方法定义待优化
public interface ScxHttpServerResponse {

    HttpStatusCode status();

    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse status(HttpStatusCode code);

    void send(byte[] data);

    void send(String data);

    void send();

    boolean closed();

}
