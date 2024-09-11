package cool.scx.http_server;

import java.util.List;

public interface ScxHttpHeaders {

    String get(ScxHttpHeaderName headerName);

    String get(String headerName);

    List<String> getAll(ScxHttpHeaderName headerName);

    List<String> getAll(String headerName);

}
