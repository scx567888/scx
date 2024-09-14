package cool.scx.http;

import java.util.List;

public interface Parameters {

    static ParametersWritable of() {
        return new ParametersImpl();
    }

    String get(String name);

    List<String> getAll(String name);

}
