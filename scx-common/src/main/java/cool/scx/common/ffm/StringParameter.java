package cool.scx.common.ffm;

import java.lang.foreign.Arena;

public class StringParameter implements Parameter {

    private final String value;

    public StringParameter(String value) {
        this.value = value;
    }

    @Override
    public Object toNativeParameter(Arena arena) {
        return arena.allocateFrom(value);
    }

}
