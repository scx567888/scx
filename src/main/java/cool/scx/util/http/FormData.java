package cool.scx.util.http;

import io.vertx.core.http.impl.MimeMapping;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class FormData {

    private static final String lineSeparator = "\r\n";

    private final List<FormDataItem> formDataItems = new ArrayList<>();

    public FormData add(String name, Object text) {
        this.formDataItems.add(new FormDataItem(name, text.toString()));
        return this;
    }

    public FormData addFile(String name, File file) throws IOException {
        this.formDataItems.add(new FormDataItem(name, file));
        return this;
    }

    public FormData addFile(String name, byte[] fileByte, String filename, String contentType) {
        this.formDataItems.add(new FormDataItem(name, fileByte, filename, contentType));
        return this;
    }

    public FormData remove(String name) {
        this.formDataItems.removeIf(formDataItem -> name.equals(formDataItem.name));
        return this;
    }

    private List<byte[]> toByteArray(FormDataItem formDataItem, String boundary) {
        var head = "--" + boundary + lineSeparator + formDataItem.getHeader() + lineSeparator + lineSeparator;
        return List.of(head.getBytes(StandardCharsets.UTF_8), formDataItem.getContent(), lineSeparator.getBytes(StandardCharsets.UTF_8));
    }

    HttpRequest.BodyPublisher getBodyPublisher(String boundary) {
        var iter = new ArrayList<byte[]>();
        for (var formDataItem : formDataItems) {
            iter.addAll(toByteArray(formDataItem, boundary));
        }
        iter.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(iter);
    }

    private static class FormDataItem {

        final String name;
        final byte[] content;
        final String filename;
        final String contentType;

        FormDataItem(String name, Object text) {
            this(name, text.toString().getBytes(StandardCharsets.UTF_8), null, null);
        }

        FormDataItem(String name, File file) throws IOException {
            this(name, Files.readAllBytes(file.toPath()), file.getName(), MimeMapping.getMimeTypeForFilename(file.getName().toLowerCase()));
        }

        FormDataItem(String name, byte[] fileByte, String filename, String contentType) {
            this.name = name;
            this.content = fileByte;
            this.filename = filename;
            this.contentType = contentType;
        }

        String getHeader() {
            var headerStr = filename == null ?
                    "Content-Disposition: form-data; name=\"" + name + "\"" :
                    "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"";
            return contentType == null ?
                    headerStr :
                    headerStr + FormData.lineSeparator + "Content-Type: " + contentType;
        }

        byte[] getContent() {
            return content;
        }

    }

}
