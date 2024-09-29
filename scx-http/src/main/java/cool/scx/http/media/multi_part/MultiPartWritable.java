package cool.scx.http.media.multi_part;

import java.nio.file.Path;

public interface MultiPartWritable extends MultiPart {

    MultiPartWritable boundary(String boundary);

    MultiPartWritable add(MultiPartPart part);

    default MultiPartWritable add(String name, String value) {
        return add(MultiPartPart.of(name, value));
    }

    default MultiPartWritable add(String name, Path value) {
        return add(MultiPartPart.of(name, value));
    }

}