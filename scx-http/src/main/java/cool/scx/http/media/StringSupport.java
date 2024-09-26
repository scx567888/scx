package cool.scx.http.media;

import cool.scx.http.ScxHttpServerRequestHeaders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StringSupport implements MediaSupport<String> {
    
    public static final StringSupport STRING_SUPPORT = new StringSupport();

    @Override
    public String read(InputStream inputStream, ScxHttpServerRequestHeaders headers) {
        var contentType = headers.contentType();
        var charset = contentType.charset();
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        try {
            var bytes = inputStream.readAllBytes();
            return new String(bytes, charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
