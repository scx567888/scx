package cool.scx.http_server;

import java.util.List;

public interface ScxHttpHeader {

    ScxHttpHeaderName name();

    List<String> allValues();

    default String value() {
        return allValues().getFirst();
    }

    default long longValue() {
        return Long.parseLong(value());
    }

}
