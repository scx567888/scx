package cool.scx.io;

import java.util.ArrayList;
import java.util.List;

/**
 * BytesDataConsumer
 *
 * @author scx567888
 * @version 0.0.1
 */
public class BytesDataConsumer implements DataConsumer {

    private List<DataNode> resultList = null;
    private DataNode result = null;
    private int total = 0;

    @Override
    public void accept(byte[] bytes, int position, int length) {
        total += length;
        if (result == null) {
            result = new DataNode(bytes, position, length);
        } else {
            if (resultList == null) {
                resultList = new ArrayList<>();
                resultList.add(result);
            }
            resultList.add(new DataNode(bytes, position, length));
        }
    }

    public byte[] getBytes() {
        if (resultList == null) {
            if (result == null) {
                return new byte[0];
            }
            return IOHelper.compressBytes(result.bytes, result.position, result.limit);
        }

        var result = new byte[total];
        int offset = 0;

        for (var b : resultList) {
            System.arraycopy(b.bytes, b.position, result, offset, b.limit);
            offset += b.limit;
        }

        return result;
    }

}
