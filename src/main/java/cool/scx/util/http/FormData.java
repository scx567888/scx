package cool.scx.util.http;

import cool.scx.exception.ScxExceptionHelper;
import io.vertx.core.http.impl.MimeMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class FormData {

    static final String lineSeparator = "\r\n";

    private final List<FormDataItem> formDataItemList = new ArrayList<>();

    public FormData add(String name, Object text) {
        this.formDataItemList.add(new FormDataItem(name, text.toString()));
        return this;
    }

    public FormData addFile(String name, File file) throws IOException {
        this.formDataItemList.add(new FormDataItem(name, file));
        return this;
    }

    public FormData addFile(String name, byte[] fileByte, String filename, String contentType) {
        this.formDataItemList.add(new FormDataItem(name, fileByte, filename, contentType));
        return this;
    }

    public FormData remove(String name) {
        this.formDataItemList.removeIf(formDataItem -> name.equals(formDataItem.name));
        return this;
    }

    HttpRequest.BodyPublisher getBodyPublisher(String boundary) {
        return HttpRequest.BodyPublishers.ofByteArrays(() -> new FormDataByteArrayIterable(formDataItemList, boundary));
    }

    enum FormDataItemType {
        TEXT, FILE, FILE_BYTE, FINAL_BOUNDARY
    }

    static final class FormDataByteArrayIterable implements Iterator<byte[]> {

        private final FormDataItem[] formDataItems;
        private final String boundary;
        private int nowIndex = 0;
        //当前状态 0 为 待发送 头 2 为待发送内容 4 为待发送 尾部
        private byte nowState;
        //如果当前文件
        private FileInputStream nowFileInputStream;

        public FormDataByteArrayIterable(List<FormDataItem> formDataItemList, String boundary) {
            this.formDataItems = new FormDataItem[formDataItemList.size() + 1];
            for (int i = 0; i < formDataItemList.size(); i++) {
                this.formDataItems[i] = formDataItemList.get(i);
            }
            //将最后一位设置为 FINAL_BOUNDARY
            this.formDataItems[this.formDataItems.length - 1] = new FormDataItem(FormDataItemType.FINAL_BOUNDARY);
            this.boundary = boundary;
        }

        @Override
        public boolean hasNext() {
            return nowIndex < formDataItems.length;
        }

        @Override
        public byte[] next() {
            //超出索引返回 null
            if (!(nowIndex < formDataItems.length)) {
                return null;
            }
            var nowItem = formDataItems[nowIndex];
            if (nowItem.type == FormDataItemType.FINAL_BOUNDARY) {
                nowState = 0;
                nowIndex = nowIndex + 1;
                return ("--" + boundary + "--").getBytes(StandardCharsets.UTF_8);
            }
            if (nowState == 0) {
                //如果是文件的话 这里开启一下 文件的流
                if (nowItem.type == FormDataItemType.FILE) {
                    nowFileInputStream = ScxExceptionHelper.wrap(() -> new FileInputStream(nowItem.file));
                }
                nowState = 2;
                return getStart(nowItem);
            } else if (nowState == 2) {
                if (nowItem.type == FormDataItemType.TEXT) {
                    nowState = 4;
                    return nowItem.text.getBytes(StandardCharsets.UTF_8);
                } else if (nowItem.type == FormDataItemType.FILE_BYTE) {
                    nowState = 4;
                    return nowItem.fileByte;
                } else if (nowItem.type == FormDataItemType.FILE) {
                    var tempByte = new byte[4096];
                    var f = ScxExceptionHelper.wrap(() -> nowFileInputStream.read(tempByte));
                    if (f > 0) {
                        return Arrays.copyOfRange(tempByte, 0, f);
                    } else {//全部读取完毕
                        ScxExceptionHelper.wrap(() -> nowFileInputStream.close());
                        nowFileInputStream = null;
                        nowState = 0;
                        nowIndex = nowIndex + 1;
                        return getEnd();
                    }
                }
            } else if (nowState == 4) {
                nowState = 0;
                nowIndex = nowIndex + 1;
                return getEnd();
            }
            return null;
        }

        public byte[] getStart(FormDataItem formDataItem) {
            var head = "--" + boundary + lineSeparator + formDataItem.getHeader() + lineSeparator + lineSeparator;
            return head.getBytes(StandardCharsets.UTF_8);
        }

        public byte[] getEnd() {
            return lineSeparator.getBytes(StandardCharsets.UTF_8);
        }

    }

    static final class FormDataItem {

        final FormDataItemType type;
        final String name;
        final String text;
        final File file;
        final byte[] fileByte;
        final String filename;
        final String contentType;

        FormDataItem(String name, Object text) {
            this.type = FormDataItemType.TEXT;
            this.name = name;
            this.text = text.toString();
            file = null;
            fileByte = null;
            filename = null;
            contentType = null;
        }

        FormDataItem(String name, File file) {
            this.type = FormDataItemType.FILE;
            this.name = name;
            this.file = file;
            this.filename = file.getName();
            this.contentType = MimeMapping.getMimeTypeForFilename(file.getName().toLowerCase());
            text = null;
            fileByte = null;
        }

        FormDataItem(String name, byte[] fileByte, String filename, String contentType) {
            this.type = FormDataItemType.FILE_BYTE;
            this.name = name;
            this.fileByte = fileByte;
            this.filename = filename;
            this.contentType = contentType;
            text = null;
            file = null;
        }

        public FormDataItem(FormDataItemType finalBoundary) {
            this.type = finalBoundary;
            this.name = null;
            this.fileByte = null;
            this.filename = null;
            this.contentType = null;
            text = null;
            file = null;
        }

        String getHeader() {
            var headerStr = filename == null ?
                    "Content-Disposition: form-data; name=\"" + name + "\"" :
                    "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"";
            return contentType == null ?
                    headerStr :
                    headerStr + FormData.lineSeparator + "Content-Type: " + contentType;
        }

    }

}
