package cool.scx.http.x.http2;

import java.util.HashMap;
import java.util.Map;

public class Http2SettingsHelper {

    public static Map<Integer, Integer> readHttp2Settings(byte[] frame) {
        // a combination of 16bit identifier and 32bits of value
        if (frame.length % 6 != 0) {
            throw new RuntimeException("Each setting must be 6 bytes, but frame size is " + frame.length);
        }

        int settingCount = frame.length / 6; // each setting is 6 bytes
        Map<Integer, Integer> values = new HashMap<>();

        for (int i = 0; i < settingCount; i++) {
            // 读取标识符
            int id = (frame[i * 6] & 0xFF) << 8 |
                    frame[i * 6 + 1] & 0xFF;
            // 读取值
            int value = ((frame[i * 6 + 2] & 0xFF) << 24) |
                    (frame[i * 6 + 3] & 0xFF) << 16 |
                    (frame[i * 6 + 4] & 0xFF) << 8 |
                    frame[i * 6 + 5] & 0xFF;
            values.put(id, value);
        }
        return values;
    }

}
