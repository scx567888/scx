package cool.scx.http;

import java.util.List;

public interface URIQuery {

    String get(String name);

    List<String> getAll(String name);

}
