package cool.scx.ffm.type.paramter;

import java.lang.foreign.Arena;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * StringParameter
 *
 * @author scx567888
 * @version 0.0.1
 */
public class StringParameter implements Parameter {

    private final String value;
    private final Charset charset;

    public StringParameter(String value) {
        this.value = value;
        this.charset = UTF_8;
    }

    public StringParameter(String value, Charset charset) {
        this.value = value;
        this.charset = charset;
    }

    @Override
    public Object toNativeParameter(Arena arena) {
        return arena.allocateFrom(value, charset);
    }

}
