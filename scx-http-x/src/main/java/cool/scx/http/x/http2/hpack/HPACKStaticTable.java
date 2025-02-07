package cool.scx.http.x.http2.hpack;

import java.util.ArrayList;
import java.util.List;

class HPACKStaticTable {
    static final List<String[]> STATIC_TABLE = new ArrayList<>();

    static {
        // Populate with static table entries (RFC 7541 Appendix A)
        STATIC_TABLE.add(new String[]{":authority", ""});
        STATIC_TABLE.add(new String[]{":authority", ""});
        STATIC_TABLE.add(new String[]{":method", "GET"});
        STATIC_TABLE.add(new String[]{":method", "POST"});
        STATIC_TABLE.add(new String[]{":path", "/"});
        STATIC_TABLE.add(new String[]{":path", "/index.html"});
        STATIC_TABLE.add(new String[]{":scheme", "http"});
        STATIC_TABLE.add(new String[]{":scheme", "https"});
        STATIC_TABLE.add(new String[]{":status", "200"});
        STATIC_TABLE.add(new String[]{":status", "204"});
        STATIC_TABLE.add(new String[]{":status", "206"});
        STATIC_TABLE.add(new String[]{":status", "304"});
        STATIC_TABLE.add(new String[]{":status", "400"});
        STATIC_TABLE.add(new String[]{":status", "404"});
        STATIC_TABLE.add(new String[]{":status", "500"});
        STATIC_TABLE.add(new String[]{"accept-charset", ""});
        STATIC_TABLE.add(new String[]{"accept-encoding", "gzip, deflate"});
        STATIC_TABLE.add(new String[]{"accept-language", ""});
        STATIC_TABLE.add(new String[]{"accept-ranges", ""});
        STATIC_TABLE.add(new String[]{"accept", ""});
        STATIC_TABLE.add(new String[]{"access-control-allow-origin", ""});
        STATIC_TABLE.add(new String[]{"age", ""});
        STATIC_TABLE.add(new String[]{"allow", ""});
        STATIC_TABLE.add(new String[]{"authorization", ""});
        STATIC_TABLE.add(new String[]{"cache-control", ""});
        STATIC_TABLE.add(new String[]{"content-disposition", ""});
        STATIC_TABLE.add(new String[]{"content-encoding", ""});
        STATIC_TABLE.add(new String[]{"content-language", ""});
        STATIC_TABLE.add(new String[]{"content-length", ""});
        STATIC_TABLE.add(new String[]{"content-location", ""});
        STATIC_TABLE.add(new String[]{"content-range", ""});
        STATIC_TABLE.add(new String[]{"content-type", ""});
        STATIC_TABLE.add(new String[]{"cookie", ""});
        STATIC_TABLE.add(new String[]{"date", ""});
        STATIC_TABLE.add(new String[]{"etag", ""});
        STATIC_TABLE.add(new String[]{"expect", ""});
        STATIC_TABLE.add(new String[]{"expires", ""});
        STATIC_TABLE.add(new String[]{"from", ""});
        STATIC_TABLE.add(new String[]{"host", ""});
        STATIC_TABLE.add(new String[]{"if-match", ""});
        STATIC_TABLE.add(new String[]{"if-modified-since", ""});
        STATIC_TABLE.add(new String[]{"if-none-match", ""});
        STATIC_TABLE.add(new String[]{"if-range", ""});
        STATIC_TABLE.add(new String[]{"if-unmodified-since", ""});
        STATIC_TABLE.add(new String[]{"last-modified", ""});
        STATIC_TABLE.add(new String[]{"link", ""});
        STATIC_TABLE.add(new String[]{"location", ""});
        STATIC_TABLE.add(new String[]{"max-forwards", ""});
        STATIC_TABLE.add(new String[]{"proxy-authenticate", ""});
        STATIC_TABLE.add(new String[]{"proxy-authorization", ""});
        STATIC_TABLE.add(new String[]{"range", ""});
        STATIC_TABLE.add(new String[]{"referer", ""});
        STATIC_TABLE.add(new String[]{"refresh", ""});
        STATIC_TABLE.add(new String[]{"retry-after", ""});
        STATIC_TABLE.add(new String[]{"server", ""});
        STATIC_TABLE.add(new String[]{"set-cookie", ""});
        STATIC_TABLE.add(new String[]{"strict-transport-security", ""});
        STATIC_TABLE.add(new String[]{"transfer-encoding", ""});
        STATIC_TABLE.add(new String[]{"user-agent", ""});
        STATIC_TABLE.add(new String[]{"vary", ""});
        STATIC_TABLE.add(new String[]{"via", ""});
        STATIC_TABLE.add(new String[]{"www-authenticate", ""});
        // Add all other static entries...
    }

    static String[] get(int index) {
        return STATIC_TABLE.get(index - 1);
    }
    
}
