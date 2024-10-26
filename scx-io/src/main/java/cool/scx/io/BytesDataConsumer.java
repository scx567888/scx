package cool.scx.io;

import java.util.ArrayList;
import java.util.List;

public class BytesDataConsumer implements DataConsumer {

    private List<BytesInfo> resultList = null;
    private BytesInfo result = null;
    private int total = 0;

    @Override
    public void accept(byte[] bytes, int position, int length) {
        total += length;
        if (result == null) {
            result = new BytesInfo(bytes, position, length);
        } else {
            if (resultList == null) {
                resultList = new ArrayList<>();
                resultList.add(result);
            }
            resultList.add(new BytesInfo(bytes, position, length));
        }
    }

    public byte[] getBytes() {
        if (resultList == null) {
            if (result == null) {
                return new byte[0];
            }
            return IOHelper.compressBytes(result.bytes, result.position, result.length);
        }

        var result = new byte[total];
        int offset = 0;

        for (var b : resultList) {
            System.arraycopy(b.bytes, b.position, result, offset, b.length);
            offset += b.length;
        }

        return result;
    }

    private record BytesInfo(byte[] bytes, int position, int length) {

    }

}
