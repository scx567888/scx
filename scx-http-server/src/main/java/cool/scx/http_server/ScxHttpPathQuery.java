package cool.scx.http_server;

import java.util.List;
import java.util.Map;

public interface ScxHttpPathQuery extends Iterable<Map.Entry<String, String>> {

    String get(String name);

    List<String> getAll(String name);

}
