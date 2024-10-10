package cool.scx.net.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamSearch {
    public static int indexOf(InputStream inputStream, byte[] pattern) throws IOException {
        if (pattern.length == 0) {
            return 0;
        }

        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            byte[] buffer = new byte[8192];
            int totalBytesRead = 0;
            int matchIndex = 0;
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    if (buffer[i] == pattern[matchIndex]) {
                        matchIndex++;
                        if (matchIndex == pattern.length) {
                            return totalBytesRead + i - matchIndex + 1;
                        }
                    } else {
                        if (matchIndex > 0) {
                            bis.mark(matchIndex);
                            i -= matchIndex; // 回退匹配索引
                            matchIndex = 0;
                            bis.reset();
                        }
                    }
                }
                totalBytesRead += bytesRead;
            }
        }
        return -1; // Pattern not found
    }
    
}
