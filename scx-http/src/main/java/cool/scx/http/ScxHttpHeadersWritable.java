package cool.scx.http;

/**
 * ScxHttpHeadersWritable
 */
public interface ScxHttpHeadersWritable extends ScxHttpHeaders, ParametersWritable<ScxHttpHeaderName, String> {

    default ScxHttpHeadersWritable set(String name, String... value) {
        set(ScxHttpHeaderName.of(name), value);
        return this;
    }

    default ScxHttpHeadersWritable add(String name, String... value) {
        add(ScxHttpHeaderName.of(name), value);
        return this;
    }

    default ScxHttpHeadersWritable remove(String name) {
        remove(ScxHttpHeaderName.of(name));
        return this;
    }

}
