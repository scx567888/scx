package cool.scx.http;

import java.util.List;

public interface Parameters {

    String get(String name);

    List<String> getAll(String name);

}
