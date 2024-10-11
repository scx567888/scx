package cool.scx.io.test.t;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamSearch {

    public static int indexOf(InputStream inputStream, byte[] pattern) throws IOException {
        try (var bis = new BufferedInputStream(inputStream)) {
            int totalBytesRead = 0;
            int matchIndex = 0;
            int bytesRead;

            while ((bytesRead = bis.read()) != -1) {
                if (bytesRead == pattern[matchIndex]) {
                    matchIndex++;
                    if (matchIndex == pattern.length) {
                        return totalBytesRead +  matchIndex + 1;
                    }
                } else {
                    matchIndex = 0;
                }
                totalBytesRead += bytesRead;
            }
        }
        return -1; // Pattern not found
    }

}
