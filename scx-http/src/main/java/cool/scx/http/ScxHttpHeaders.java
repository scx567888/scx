package cool.scx.http;

import java.util.List;
import java.util.Map;

public interface ScxHttpHeaders extends Iterable<Map.Entry<String, List<String>>> {

    String get(ScxHttpHeaderName headerName);

    String get(String headerName);

    List<String> getAll(ScxHttpHeaderName headerName);

    List<String> getAll(String headerName);

}
