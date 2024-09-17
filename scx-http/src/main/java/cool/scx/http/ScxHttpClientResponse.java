package cool.scx.http;

//todo 
public interface ScxHttpClientResponse {

    HttpStatusCode statusCode();

    ScxHttpHeaders headers();

    ScxHttpBody body();

}
