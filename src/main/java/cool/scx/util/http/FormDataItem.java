package cool.scx.util.http;

import io.vertx.core.http.impl.MimeMapping;

import java.io.File;

final class FormDataItem {
    FormDataItemType type;
    String name;
    String text;
    File file;
    byte[] fileByte;
    String filename;
    String contentType;

    FormDataItem(FormDataItemType type) {
        this.type = type;
    }

    FormDataItem(String name, Object text) {
        this.type = FormDataItemType.TEXT;
        this.name = name;
        this.text = text.toString();
    }

    FormDataItem(String name, File file) {
        this.type = FormDataItemType.FILE;
        this.name = name;
        this.file = file;
        this.filename = file.getName();
        this.contentType = MimeMapping.getMimeTypeForFilename(filename);
    }

    FormDataItem(String name, byte[] fileByte, String filename, String contentType) {
        this.type = FormDataItemType.FILE_BYTE;
        this.name = name;
        this.fileByte = fileByte;
        this.filename = filename;
        this.contentType = contentType;
    }

    FormDataItem(String name, byte[] fileByte, String filename) {
        this.type = FormDataItemType.FILE_BYTE;
        this.name = name;
        this.fileByte = fileByte;
        this.filename = filename;
        this.contentType = MimeMapping.getMimeTypeForFilename(filename);
    }

}