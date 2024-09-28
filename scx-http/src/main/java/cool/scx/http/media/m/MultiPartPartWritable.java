package cool.scx.http.media.m;

import cool.scx.http.ScxHttpHeadersWritable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public interface MultiPartPartWritable extends MultiPartPart {

    MultiPartPartWritable headers(ScxHttpHeadersWritable headers);

    MultiPartPartWritable body(Supplier<InputStream> os);

    default MultiPartPartWritable body(InputStream os) {
        return body(() -> os);
    }

    default MultiPartPartWritable body(byte[] os) {
        return body(() -> new ByteArrayInputStream(os));
    }

    default MultiPartPartWritable body(String os) {
        return body(() -> new ByteArrayInputStream(os.getBytes()));
    }

    default MultiPartPartWritable body(Path os) {
        return body(() -> {
            try {
                return Files.newInputStream(os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
