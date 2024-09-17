package cool.scx.http;

import cool.scx.http.uri.ScxURI;

import java.io.IOException;

//todo 
public interface ScxHttpClient {

    ScxHttpClientResponse request(ScxHttpClientRequest request) throws IOException, InterruptedException;

    ScxClientWebSocket webSocket(ScxURI uri);

}
