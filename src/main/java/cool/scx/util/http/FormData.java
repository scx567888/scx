package cool.scx.util.http;

import io.vertx.core.http.impl.MimeMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public final class FormData implements Iterable<FormData.FormDataItem> {

    private final List<FormDataItem> formDataItems = new ArrayList<>();

    /**
     * <p>add.</p>
     *
     * @param name a {@link String} object
     * @param text a {@link Object} object
     * @return a {@link FormDataBodyPublisherBuilder} object
     */
    public FormData add(String name, Object text) {
        formDataItems.add(new FormDataItem(name, FormDataItemType.TEXT, text.toString(), null));
        return this;
    }

    /**
     * todo 这里还没有处理重复键问题
     * <p>addFile.</p>
     *
     * @param name a {@link String} object
     * @param file a {@link File} object
     * @return a {@link FormDataBodyPublisherBuilder} object
     */
    public FormData addFile(String name, File file) {
        formDataItems.add(new FormDataItem(name, FormDataItemType.FILE, null, file));
        return this;
    }

    @Override
    public Iterator<FormDataItem> iterator() {
        return formDataItems.iterator();
    }

    @Override
    public void forEach(Consumer<? super FormDataItem> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<FormDataItem> spliterator() {
        return Iterable.super.spliterator();
    }

    /**
     * 表单项类型 文字 或 文件
     */
    public enum FormDataItemType {
        TEXT, FILE
    }

    /**
     * 表单项
     */
    public static final class FormDataItem {

        public final String name;
        public final FormDataItemType type;
        public final String text;
        public final File file;
        public final String filename;
        public final String contentType;

        public FormDataItem(String name, FormDataItemType type, String text, File file) {
            this.name = name;
            this.type = type;
            this.text = text;
            this.file = file;
            if (type == FormDataItemType.FILE) {
                this.filename = file.getName();
                this.contentType = MimeMapping.getMimeTypeForFilename(this.filename.toLowerCase());
            } else {
                this.filename = null;
                this.contentType = null;
            }
        }

    }

}
