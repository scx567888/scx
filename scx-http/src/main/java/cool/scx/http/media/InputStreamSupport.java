package cool.scx.http.media;

import cool.scx.http.ScxHttpServerRequestHeaders;

import java.io.InputStream;

public class InputStreamSupport implements MediaSupport<InputStream>{
    
    public static final InputStreamSupport INPUT_STREAM_SUPPORT = new InputStreamSupport();

    @Override
    public InputStream read(InputStream inputStream, ScxHttpServerRequestHeaders headers) {
        return inputStream;
    }
    
}
