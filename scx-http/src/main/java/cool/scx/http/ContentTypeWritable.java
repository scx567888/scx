package cool.scx.http;

public interface ContentTypeWritable extends ContentType {

    ContentTypeWritable mediaType(ScxMediaType mediaType);

    ContentTypeWritable params(Parameters parameters);

}
