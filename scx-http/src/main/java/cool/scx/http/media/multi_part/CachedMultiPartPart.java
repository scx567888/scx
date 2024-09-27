package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeadersWritable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CachedMultiPartPart extends MultiPartPart {

    private final Path contentPath;

    public CachedMultiPartPart(ScxHttpHeadersWritable headers, byte[] content, Path contentPath) {
        super(headers, content);
        this.contentPath = contentPath;
    }

    @Override
    public byte[] content() {
        if (content != null) {
            return content;
        }
        try {
            return Files.readAllBytes(contentPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path contentPath() {
        return contentPath;
    }

    public void deleteContentPath() {
        try {
            Files.delete(contentPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
