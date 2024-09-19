package cool.scx.http;

public interface ScxMediaType {

    static ScxMediaType of(String str) {
        try {
            return MediaType.of(str);
        } catch (IllegalArgumentException e) {
            var s = str.split("/");
            if (s.length == 2) {
                return new ScxMediaTypeImpl(s[0], s[1]);
            }
            throw new IllegalArgumentException("Invalid media type: " + str);
        }
    }

    String type();

    String subtype();

    String value();

}
