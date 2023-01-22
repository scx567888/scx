package cool.scx.http_client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class ScxHttpClientResponseBody {

    private final byte[] bytes;

    public ScxHttpClientResponseBody(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return toString(StandardCharsets.UTF_8);
    }

    public String toString(Charset charset) {
        return new String(bytes, charset);
    }

    public Path toFile(Path path, OpenOption... options) throws IOException {
        return Files.write(path, bytes, options);
    }

    public byte[] toBytes() {
        return bytes;
    }

}
