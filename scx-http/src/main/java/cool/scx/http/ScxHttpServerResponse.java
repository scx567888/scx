package cool.scx.http;

//todo 方法定义待优化
public interface ScxHttpServerResponse {

    HttpStatusCode statusCode();

    ScxHttpHeadersWritable headers();

    ScxHttpServerResponse setStatusCode(HttpStatusCode code);

    void send(byte[] data);

    void send(String data);

    void send();

    boolean closed();

    default ScxHttpServerResponse setHeader(ScxHttpHeaderName headerName, String... values) {
        this.headers().set(headerName, values);
        return this;
    }

}
