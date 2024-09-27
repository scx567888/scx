package cool.scx.http.media.string;

import cool.scx.http.media.MediaReader;
import cool.scx.http.media.MediaSupport;

import static cool.scx.http.media.string.StringReader.STRING_READER;

public final class StringSupport implements MediaSupport<String> {

    public static final StringSupport STRING_SUPPORT = new StringSupport();

    @Override
    public MediaReader<String> reader() {
        return STRING_READER;
    }

}
