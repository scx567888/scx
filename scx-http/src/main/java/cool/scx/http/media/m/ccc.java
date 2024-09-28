package cool.scx.http.media.m;

import javax.xml.transform.Source;
import java.io.IOException;
import java.nio.file.Path;

public class ccc {
    
    public static void main(String[] args) throws IOException {
        var multiPartParts = MultiPart.of("123132");
        multiPartParts.add(MultiPartPart.of().body(Path.of("123")));
        for (MultiPartPart multiPartPart : multiPartParts) {
            System.out.println(new String(multiPartPart.body().get().readAllBytes()));
        }
        for (MultiPartPart multiPartPart : multiPartParts) {
            System.out.println(new String(multiPartPart.body().get().readAllBytes()));
        }
    }
    
}
