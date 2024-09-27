package cool.scx.http.media.input_stream;

import cool.scx.http.ScxHttpServerRequestHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

/**
 * 此类保持单例模式即可
 */
public class InputStreamReader implements MediaReader<InputStream> {

    public final static InputStreamReader INPUT_STREAM_READER = new InputStreamReader();

    private InputStreamReader() {

    }

    @Override
    public InputStream read(InputStream inputStream, ScxHttpServerRequestHeaders headers) {
        return inputStream;
    }

}
