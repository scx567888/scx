package cool.scx.http_server;

import java.util.List;

public interface URIQuery {

    String get(String name);

    List<String> getAll(String name);

}
