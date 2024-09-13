package cool.scx.web.type;

import io.helidon.http.media.multipart.MultiPart;

import java.util.HashMap;
import java.util.Map;

public class FormData {

    private final Map<String, FormDataPart> parts;

    public FormData(MultiPart multiPart) {
        this.parts = new HashMap<>();
        while (multiPart.hasNext()) {
            var next = multiPart.next();
            var part = new FormDataPart(next);
            this.parts.put(part.name(), part);
        }
    }

    public FormDataPart get(String name) {
        return parts.get(name);
    }

}
