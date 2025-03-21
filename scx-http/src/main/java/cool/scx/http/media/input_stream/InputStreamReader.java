package cool.scx.http.media.input_stream;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media.MediaReader;

import java.io.InputStream;

/// 此类保持单例模式即可
///
/// @author scx567888
/// @version 0.0.1
public final class InputStreamReader implements MediaReader<InputStream> {

    public final static InputStreamReader INPUT_STREAM_READER = new InputStreamReader();

    private InputStreamReader() {

    }

    @Override
    public InputStream read(InputStream inputStream, ScxHttpHeaders headers) {
        return inputStream;
    }

}
