package cool.scx.http.media.multi_part;

import cool.scx.common.util.RandomUtils;

public interface MultiPart extends Iterable<MultiPartPart> {

    static MultiPartWritable of(String boundary) {
        return new MultiPartImpl(boundary);
    }

    static MultiPartWritable of() {
        return new MultiPartImpl("scx" + RandomUtils.randomString(10));
    }

    String boundary();

}
