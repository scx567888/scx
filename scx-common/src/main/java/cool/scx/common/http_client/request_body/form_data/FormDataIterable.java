package cool.scx.common.http_client.request_body.form_data;

import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

import java.util.Iterator;

public class FormDataIterable implements Iterable<byte[]> {

    private final FormDataIterator iterator;

    public FormDataIterable(HttpPostRequestEncoder encoder) {
        this.iterator = new FormDataIterator(encoder);
    }

    @Override
    public Iterator<byte[]> iterator() {
        return iterator;
    }

}
