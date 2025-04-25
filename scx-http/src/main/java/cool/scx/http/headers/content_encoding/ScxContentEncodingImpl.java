package cool.scx.http.headers.content_encoding;

record ScxContentEncodingImpl(String value) implements ScxContentEncoding {

    ScxContentEncodingImpl {
        value = value.toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }

}
