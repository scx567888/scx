package cool.scx.ffm.mapper;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.UTF_8;

/// StringMapper
///
/// @author scx567888
/// @version 0.0.1
public class StringMapper implements Mapper {

    private final String value;
    private final Charset charset;

    public StringMapper(String value) {
        this.value = value;
        this.charset = UTF_8;
    }

    public StringMapper(String value, Charset charset) {
        this.value = value;
        this.charset = charset;
    }

    @Override
    public MemorySegment toMemorySegment(Arena arena) {
        return arena.allocateFrom(value, charset);
    }

    @Override
    public void fromMemorySegment(MemorySegment memorySegment) {

    }

}
