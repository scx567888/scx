package cool.scx.http.media.multi_part;

import cool.scx.http.media.MediaReader;
import cool.scx.http.media.MediaSupport;

import static cool.scx.http.media.multi_part.MultiPartReader.MULTI_PART_READER;

public class MultiPartSupport implements MediaSupport<MultiPart> {

    public static final MultiPartSupport MULTI_PART_SUPPORT = new MultiPartSupport();

    @Override
    public MediaReader<MultiPart> reader() {
        return MULTI_PART_READER;
    }

}
