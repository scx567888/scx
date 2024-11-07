package cool.scx.io.source2;

import java.io.ByteArrayInputStream;

public class ByteArrayInputSource extends InputStreamInputSource {

    public ByteArrayInputSource(byte[] bytes) {
        super(new ByteArrayInputStream(bytes));
    }

}
