package cool.scx.web.type;

import cool.scx.common.util.MultiMap;
import io.helidon.http.media.multipart.MultiPart;

import java.util.List;

public class FormData {

    private final MultiMap<String, FormDataPart> parts;

    public FormData(MultiPart multiPart) {
        this.parts = new MultiMap<>();
        while (multiPart.hasNext()) {
            var next = multiPart.next();
            var part = new FormDataPart(next);
            this.parts.put(part.name(), part);
        }
    }

    public FormDataPart get(String name) {
        return parts.getFirst(name);
    }

    public List<FormDataPart> getAll(String name) {
        return parts.get(name);
    }

}
