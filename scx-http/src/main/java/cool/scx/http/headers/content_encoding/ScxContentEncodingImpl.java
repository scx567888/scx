package cool.scx.http.headers.content_encoding;

record ScxContentEncodingImpl(String value) implements ScxContentEncoding {

    @Override
    public String toString() {
        return value;
    }

}
