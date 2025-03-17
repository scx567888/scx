package cool.scx.http.headers.transfer_encoding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
        var split = transferEncodingHeader.split(",");
        var list = new ArrayList<ScxEncodingType>();
        for (var s : split) {
            list.add(ScxEncodingType.of(s));
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
        return encodingTypes.stream().map(ScxEncodingType::value).collect(Collectors.joining(","));
    }
    
}
