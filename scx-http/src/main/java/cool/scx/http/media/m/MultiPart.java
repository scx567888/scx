package cool.scx.http.media.m;

public interface MultiPart extends Iterable<MultiPartPart> {

    static MultiPartWritable of(String boundary) {
        return new MultiPartImpl(boundary);
    }

    String boundary();

}
