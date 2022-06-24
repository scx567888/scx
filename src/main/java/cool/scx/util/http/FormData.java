package cool.scx.util.http;

import cool.scx.util.RandomUtils;
import cool.scx.util.exception.ScxExceptionHelper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.impl.MimeMapping;

import java.io.File;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * a
 *
 * @author scx567888
 * @version 1.11.8
 */
public final class FormData implements Body {

    /**
     * a
     */
    private static final String FORM_BOUNDARY_PREFIX = "----ScxHttpClientHelperFormBoundary";

    /**
     * Constant <code>lineSeparator="\r\n"</code>
     */
    private static final String lineSeparator = "\r\n";

    /**
     * Constant <code>lineSeparatorByteArray</code>
     */
    private static final byte[] lineSeparatorByteArray = lineSeparator.getBytes(StandardCharsets.UTF_8);

    private final List<FormDataItem> formDataItemList = new ArrayList<>();

    private static byte[] getStart(FormDataItem f, String boundary) {
        var headerStr = f.filename == null ?
                "Content-Disposition: form-data; name=\"" + f.name + "\"" :
                "Content-Disposition: form-data; name=\"" + f.name + "\"; filename=\"" + f.filename + "\"";
        var finalHeader = f.contentType == null ?
                headerStr :
                headerStr + lineSeparator + "Content-Type: " + f.contentType;
        var start = "--" + boundary + lineSeparator + finalHeader + lineSeparator + lineSeparator;
        return start.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] getContent(FormDataItem nowItem) {
        return switch (nowItem.type) {
            case CONTENT -> nowItem.content;
            case FILE -> ScxExceptionHelper.wrap(() -> Files.readAllBytes(nowItem.file.toPath()));
        };
    }

    private static byte[] getEnd(String boundary) {
        var end = "--" + boundary + "--";
        return end.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * a
     *
     * @param name a
     * @param text a
     * @return a
     */
    public FormData add(String name, Object text) {
        this.formDataItemList.add(new FormDataItem(name, text.toString()));
        return this;
    }

    /**
     * a
     *
     * @param name a
     * @param file a
     * @return a
     */
    public FormData addFile(String name, File file) {
        this.formDataItemList.add(new FormDataItem(name, file));
        return this;
    }

    /**
     * a
     *
     * @param name        a
     * @param fileByte    a
     * @param filename    a
     * @param contentType a
     * @return a
     */
    public FormData addFile(String name, byte[] fileByte, String filename, String contentType) {
        this.formDataItemList.add(new FormDataItem(name, fileByte, filename, contentType));
        return this;
    }

    /**
     * a
     *
     * @param name     a
     * @param fileByte a
     * @param filename a
     * @return a
     */
    public FormData addFile(String name, byte[] fileByte, String filename) {
        this.formDataItemList.add(new FormDataItem(name, fileByte, filename));
        return this;
    }

    /**
     * a
     *
     * @param name a
     * @return a
     */
    public FormData remove(String name) {
        this.formDataItemList.removeIf(formDataItem -> name.equals(formDataItem.name));
        return this;
    }

    /**
     * a
     *
     * @param builder a a
     * @return a
     */
    @Override
    public HttpRequest.BodyPublisher getBodyPublisher(HttpRequest.Builder builder) {
        final String boundary = FORM_BOUNDARY_PREFIX + RandomUtils.getRandomString(8, true);
        builder.setHeader("content-type", "multipart/form-data; boundary=" + boundary);
        var buffer = Buffer.buffer();
        for (var formDataItem : formDataItemList) {
            buffer.appendBytes(getStart(formDataItem, boundary));
            buffer.appendBytes(getContent(formDataItem));
            buffer.appendBytes(lineSeparatorByteArray);
        }
        buffer.appendBytes(getEnd(boundary));
        return HttpRequest.BodyPublishers.ofByteArray(buffer.getBytes());
    }

    enum FormDataItemType {
        CONTENT, FILE
    }

    static final class FormDataItem {
        final FormDataItemType type;
        String name;
        File file;
        byte[] content;
        String filename;
        String contentType;

        FormDataItem(String name, Object text) {
            this.type = FormDataItemType.CONTENT;
            this.name = name;
            this.content = text.toString().getBytes(StandardCharsets.UTF_8);
        }

        FormDataItem(String name, byte[] content, String filename, String contentType) {
            this.type = FormDataItemType.CONTENT;
            this.name = name;
            this.content = content;
            this.filename = filename;
            this.contentType = contentType;
        }

        FormDataItem(String name, byte[] content, String filename) {
            this.type = FormDataItemType.CONTENT;
            this.name = name;
            this.content = content;
            this.filename = filename;
            this.contentType = MimeMapping.getMimeTypeForFilename(this.filename);
        }

        FormDataItem(String name, File file) {
            this.type = FormDataItemType.FILE;
            this.name = name;
            this.file = file;
            this.filename = file.getName();
            this.contentType = MimeMapping.getMimeTypeForFilename(this.filename);
        }

    }

}
