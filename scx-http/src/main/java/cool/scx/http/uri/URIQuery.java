package cool.scx.http.uri;

import java.util.List;

public interface URIQuery {

    String get(String name);

    List<String> getAll(String name);

}
