package cool.scx.http.media.m;

import cool.scx.http.ScxHttpHeaders;

import java.io.InputStream;
import java.util.function.Supplier;

public interface MultiPartPart {

    static MultiPartPartWritable of() {
        return new MultiPartPartImpl();
    }

    ScxHttpHeaders headers();

    Supplier<InputStream> body();

}
