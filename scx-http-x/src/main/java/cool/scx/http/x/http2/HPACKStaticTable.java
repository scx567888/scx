package cool.scx.http.x.http2;

public class HPACKStaticTable {

    public static final String[][] HPACK_STATIC_TABLE = {
            null,//HPACK 静态表 索引从 1 开始 这个 null 是用来占位的
            {":authority", ""},
            {":method", "GET"},
            {":method", "POST"},
            {":path", "/"},
            {":path", "/index.html"},
            {":scheme", "http"},
            {":scheme", "https"},
            {":status", "200"},
            {":status", "204"},
            {":status", "206"},
            {":status", "304"},
            {":status", "400"},
            {":status", "404"},
            {":status", "500"},
            {"accept-charset", ""},
            {"accept-encoding", "gzip, deflate"},
            {"accept-language", ""},
            {"accept-ranges", ""},
            {"accept", ""},
            {"access-control-allow-origin", ""},
            {"age", ""},
            {"allow", ""},
            {"authorization", ""},
            {"cache-control", ""},
            {"content-disposition", ""},
            {"content-encoding", ""},
            {"content-language", ""},
            {"content-length", ""},
            {"content-location", ""},
            {"content-range", ""},
            {"content-type", ""},
            {"cookie", ""},
            {"date", ""},
            {"etag", ""},
            {"expect", ""},
            {"expires", ""},
            {"from", ""},
            {"host", ""},
            {"if-match", ""},
            {"if-modified-since", ""},
            {"if-none-match", ""},
            {"if-range", ""},
            {"if-unmodified-since", ""},
            {"last-modified", ""},
            {"link", ""},
            {"location", ""},
            {"max-forwards", ""},
            {"proxy-authenticate", ""},
            {"proxy-authorization", ""},
            {"range", ""},
            {"referer", ""},
            {"refresh", ""},
            {"retry-after", ""},
            {"server", ""},
            {"set-cookie", ""},
            {"strict-transport-security", ""},
            {"transfer-encoding", ""},
            {"user-agent", ""},
            {"vary", ""},
            {"via", ""},
            {"www-authenticate", ""}
    };

    public static String[] ofIndex(int index) {
        return HPACK_STATIC_TABLE[index];
    }

}
