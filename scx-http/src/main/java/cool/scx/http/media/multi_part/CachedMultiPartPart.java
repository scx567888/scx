package cool.scx.http.media.multi_part;

import cool.scx.http.ScxHttpHeadersWritable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CachedMultiPartPart extends MultiPartPart {

    private final Path contentPath;
    private byte[] cacheContent = null;

    public CachedMultiPartPart(ScxHttpHeadersWritable headers, byte[] content, Path contentPath) {
        super(headers, content);
        this.contentPath = contentPath;
    }

    @Override
    public byte[] content() {
        if (content != null) {
            return content;
        }
        if (cacheContent == null) {
            try {
                cacheContent = Files.readAllBytes(contentPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return cacheContent;
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
