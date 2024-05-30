package cool.scx.common.http_client.request_body.form_data;

import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

import java.util.Iterator;

import static cool.scx.common.util.ScxExceptionHelper.wrap;

class FormDataIterator implements Iterator<byte[]> {

    private static final UnpooledByteBufAllocator ALLOC = new UnpooledByteBufAllocator(false);

    private final HttpPostRequestEncoder encoder;

    public FormDataIterator(HttpPostRequestEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public boolean hasNext() {
        return wrap(() -> !this.encoder.isEndOfInput());
    }

    @Override
    public byte[] next() {
        return wrap(() -> {
            var chunk = encoder.readChunk(ALLOC);
            var content = chunk.content();
            byte[] arr = new byte[content.writerIndex()];
            content.getBytes(0, arr);
            return arr;
        });
    }

}
