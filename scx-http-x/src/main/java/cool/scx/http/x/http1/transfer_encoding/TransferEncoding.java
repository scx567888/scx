package cool.scx.http.x.http1.transfer_encoding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static cool.scx.http.media_type.ScxMediaTypeHelper.SEMICOLON_PATTERN;
import static java.util.Collections.addAll;

/// todo 没有做 分块为最后一个的校验
public class TransferEncoding implements Iterable<ScxEncodingType> {

    private final List<ScxEncodingType> encodingTypes;

    public TransferEncoding(List<ScxEncodingType> encodingTypes) {
        this.encodingTypes = encodingTypes;
    }

    public TransferEncoding(ScxEncodingType... encodingTypes) {
        this.encodingTypes = new ArrayList<>(encodingTypes.length);
        addAll(this.encodingTypes, encodingTypes);
    }

    public static TransferEncoding parseTransferEncoding(String transferEncodingHeader) {
        var split = SEMICOLON_PATTERN.split(transferEncodingHeader);
        var list = new ArrayList<ScxEncodingType>();
        for (var s : split) {
            list.add(ScxEncodingType.of(s.trim()));
        }
        return new TransferEncoding(list);
    }

    public List<ScxEncodingType> encodingTypes() {
        return encodingTypes;
    }

    @Override
    public Iterator<ScxEncodingType> iterator() {
        return encodingTypes.iterator();
    }

    public String encode() {
        return encodingTypes.stream().map(ScxEncodingType::value).collect(Collectors.joining(", "));
    }

    public int size() {
        return encodingTypes.size();
    }

    public ScxEncodingType get(int i) {
        return encodingTypes.get(i);
    }

}
