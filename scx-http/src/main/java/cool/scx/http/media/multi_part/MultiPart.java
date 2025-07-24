package cool.scx.http.media.multi_part;

import cool.scx.common.util.RandomUtils;

import static cool.scx.common.constant.CharPools.NUMBER_AND_LOWER_LETTER;

/// MultiPart
///
/// @author scx567888
/// @version 0.0.1
public interface MultiPart extends Iterable<MultiPartPart> {

    static MultiPartWritable of(String boundary) {
        return new MultiPartImpl(boundary);
    }

    static MultiPartWritable of() {
        return new MultiPartImpl("scx" + RandomUtils.randomString(10, NUMBER_AND_LOWER_LETTER));
    }

    String boundary();

}
