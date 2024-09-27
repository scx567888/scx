package cool.scx.http.media.input_stream;

import cool.scx.http.media.MediaReader;
import cool.scx.http.media.MediaSupport;

import java.io.InputStream;

import static cool.scx.http.media.input_stream.InputStreamReader.INPUT_STREAM_READER;

/**
 * 此类保持单例模式即可
 */
public final class InputStreamSupport implements MediaSupport<InputStream> {

    public static final InputStreamSupport INPUT_STREAM_SUPPORT = new InputStreamSupport();

    private InputStreamSupport() {

    }

    @Override
    public MediaReader<InputStream> reader() {
        return INPUT_STREAM_READER;
    }

}
