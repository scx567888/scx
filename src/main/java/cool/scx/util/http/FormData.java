package cool.scx.util.http;

import java.io.File;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

public final class FormData {

    static final String lineSeparator = "\r\n";

    private final List<FormDataItem> formDataItemList = new ArrayList<>();

    public FormData add(String name, Object text) {
        this.formDataItemList.add(new FormDataItem(name, text.toString()));
        return this;
    }

    public FormData addFile(String name, File file) {
        this.formDataItemList.add(new FormDataItem(name, file));
        return this;
    }

    public FormData addFile(String name, byte[] fileByte, String filename, String contentType) {
        this.formDataItemList.add(new FormDataItem(name, fileByte, filename, contentType));
        return this;
    }

    public FormData addFile(String name, byte[] fileByte, String filename) {
        this.formDataItemList.add(new FormDataItem(name, fileByte, filename));
        return this;
    }

    public FormData remove(String name) {
        this.formDataItemList.removeIf(formDataItem -> name.equals(formDataItem.name));
        return this;
    }

    HttpRequest.BodyPublisher getBodyPublisher(String boundary) {
        return HttpRequest.BodyPublishers.ofByteArrays(() -> new FormDataByteArrayIterable(formDataItemList, boundary));
    }

}
