package cool.scx.net.buffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class KMPInputStream extends InputStream {
    private final InputStream proxy;
    private final byte[] buffer;
    private int pos;
    private int count;

    public KMPInputStream(InputStream proxy) {
        this.proxy = proxy;
        this.buffer = new byte[8192];
        this.pos = 0;
        this.count = 0;
    }

    @Override
    public int read() throws IOException {
        if (pos >= count) {
            count = proxy.read(buffer);
            pos = 0;
            if (count == -1) {
                return -1;
            }
        }
        return buffer[pos++] & 0xff;
    }

    public static int[] computeLPSArray(byte[] pattern) {
        int length = 0;
        int i = 1;
        int[] lps = new int[pattern.length];
        lps[0] = 0; 

        while (i < pattern.length) {
            if (pattern[i] == pattern[length]) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public int indexOf(byte[] pattern) throws IOException {
        int[] lps = computeLPSArray(pattern);
        int i = 0; 
        int j = 0;

        while (true) {
            int byteRead = read();
            if (byteRead == -1) {
                return -1;
            }

            if (pattern[j] == (byte) byteRead) {
                i++;
                j++;
                if (j == pattern.length) {
                    return i - j;
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
            
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = 0;
        while (len > 0) {
            int byteRead = read();
            if (byteRead == -1) {
                break;
            }
            b[off++] = (byte) byteRead;
            len--;
            bytesRead++;
        }
        return bytesRead == 0 ? -1 : bytesRead;
    }

    @Override
    public boolean markSupported() {
        return false;
    }
    
    @Override
    public void close() throws IOException {
        proxy.close();
    }

//    public static void main(String[] args) {
//        var s=new ByteArrayInputStream("aaaaaaaaaaaaaaaaaaaaaaaaaaab\r\n".getBytes());
//        var s1=new KMPInputStream(s);
//        try {
//            int i = s1.indexOf("\r\b".getBytes());
//            System.out.println(i);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    
}
