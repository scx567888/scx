package cool.scx.http_client.body.form_data;

import cool.scx.util.RandomUtils;

import java.nio.file.Path;

import static cool.scx.util.StringUtils.notBlank;
import static io.vertx.core.http.impl.MimeMapping.getMimeTypeForExtension;
import static io.vertx.core.http.impl.MimeMapping.getMimeTypeForFilename;
import static java.util.Objects.requireNonNull;

public final class FormDataItem {

    private final FormDataItemType type;
    private final String name;
    private final String filename;
    private final String contentType;
    private String attributeValue;
    private Path fileUploadPath;
    private byte[] fileUploadPathBytes;

    public FormDataItem(String name, String value) {
        this.type = FormDataItemType.ATTRIBUTE;
        this.name = requireNonNull(name);
        this.attributeValue = value;
        this.filename = null;
        this.contentType = null;
    }

    public FormDataItem(String name, byte[] content) {
        this(name, content, RandomUtils.randomUUID());
    }

    public FormDataItem(String name, byte[] content, String filename) {
        this(name, content, filename, getMimeTypeForFilename(filename));
    }

    public FormDataItem(String name, byte[] content, String filename, String contentType) {
        this.type = FormDataItemType.FILE_UPLOAD_BYTES;
        this.name = requireNonNull(name);
        this.fileUploadPathBytes = content;
        this.filename = notBlank(filename) ? filename : RandomUtils.randomUUID();
        this.contentType = notBlank(contentType) ? contentType : getMimeTypeForExtension("bin");
    }

    public FormDataItem(String name, Path filePath) {
        this(name, filePath, filePath.getFileName().toString());
    }

    public FormDataItem(String name, Path filePath, String filename) {
        this(name, filePath, filename, getMimeTypeForFilename(filename));
    }

    public FormDataItem(String name, Path filePath, String filename, String contentType) {
        this.type = FormDataItemType.FILE_UPLOAD_PATH;
        this.name = requireNonNull(name);
        this.fileUploadPath = filePath;
        this.filename = notBlank(filename) ? filename : RandomUtils.randomUUID();
        this.contentType = notBlank(contentType) ? contentType : getMimeTypeForExtension("bin");
    }

    public String name() {
        return name;
    }

    public String attributeValue() {
        return attributeValue;
    }

    FormDataItemType type() {
        return type;
    }

    public String filename() {
        return filename;
    }

    public Path fileUploadPath() {
        return fileUploadPath;
    }

    public String contentType() {
        return contentType;
    }

    public byte[] fileUploadPathBytes() {
        return fileUploadPathBytes;
    }

}
