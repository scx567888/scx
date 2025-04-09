package cool.scx.http.test;

import cool.scx.http.headers.ScxHttpHeaders;
import cool.scx.http.media_type.MediaType;
import cool.scx.http.media_type.ScxMediaType;

import java.nio.charset.StandardCharsets;

public class HeadersTest {

    public static void main(String[] args) {
        test1();
    }

    public static void test1() {
        long l = System.nanoTime();
        for (int i = 0; i < 9999; i = i + 1) {
            //可以直接从一个现有的内容构建
            var h = ScxHttpHeaders.of("""
                    Accept: */*
                    Accept-Encoding: gzip, deflate, br, zstd
                    Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6
                    Connection: keep-alive
                    Content-Length: 25
                    Content-Type: application/x-www-form-urlencoded
                    Cookie: PSTM=1740102761; BIDUPSID=5381D5240DEDCF164305D7433F5BE86A; BAIDUID=F71FAC59EBF8EAC8B3CB9C3EE9CCE73F:FG=1; newlogin=1; BDUSS=WdVelFqdzlwQmJ4NmNZMkczUDhPV2haSlcyc3Z0V29WcXd5dmYyckJuaFRVZ3RvRVFBQUFBJCQAAAAAAQAAAAEAAAD-Fdh3c2N4NTY3ODg5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFPF42dTxeNnZ; BDUSS_BFESS=WdVelFqdzlwQmJ4NmNZMkczUDhPV2haSlcyc3Z0V29WcXd5dmYyckJuaFRVZ3RvRVFBQUFBJCQAAAAAAQAAAAEAAAD-Fdh3c2N4NTY3ODg5AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFPF42dTxeNnZ; ZFY=AxqiFM6PXjI89gBZ7r4lHfX0bzciEdX7besIdXCYmY0:C; BAIDUID_BFESS=F71FAC59EBF8EAC8B3CB9C3EE9CCE73F:FG=1; H_WISE_SIDS=61027_62325_62338_62701_62520_62329_62788_62823_62842_62863; BDRCVFR[dDFRJWj44Gm]=I67x6TjHwwYf0; H_PS_PSSID=61027_62325_62338_62701_62520_62329_62788_62823_62842_62863_62878_62880_62892; delPer=0; PSINO=1; BA_HECTOR=0ga12g0h240g80ak2kag84ak2kmi2s1jvblu023; BDORZ=FFFB88E999055A3F8A630C64834BD6D0; ab_sr=1.0.1_NDA5YmIyNThkNzhhOWZjOTVhY2M1YzI4NThkMDg1NWVhM2NhNzgyOTM4YWQ1ZTkwNjQxYjNlYmFmNzIwNDgwMmZlMDJmYWRkZjNiZWUxOWUyODY0NDhkM2QzZjNlYzRjZjk3OGRkYWIxNTc3ZTVjOGU4YWRlYTgzN2E2ZmYxYTliNTI5OGNiMzU0MGM1MDUwMTAxOGJmZDdhMjFlZTQ5Yg==; RT="z=1&dm=baidu.com&si=fcff274d-de66-4505-97b6-f09b8c838670&ss=m99ddfqa&sl=2&tt=406&bcn=https%3A%2F%2Ffclog.baidu.com%2Flog%2Fweirwood%3Ftype%3Dperf&ld=5xe"
                    Host: fanyi.baidu.com
                    Origin: https://fanyi.baidu.com
                    Referer: https://fanyi.baidu.com/mtpe-individual/multimodal?query=%E5%86%92%E5%8F%B7&lang=zh2en
                    Sec-Fetch-Dest: empty
                    Sec-Fetch-Mode: cors
                    Sec-Fetch-Site: same-origin
                    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0
                    sec-ch-ua: "Microsoft Edge";v="135", "Not-A.Brand";v="8", "Chromium";v="135"
                    sec-ch-ua-mobile: ?0
                    sec-ch-ua-platform: "Windows"
                    """);
            h.add("Content-Disposition", "form-data; name=myname");
            h.contentLength(100);
            h.contentType(ScxMediaType.of(MediaType.APPLICATION_JSON).charset(StandardCharsets.UTF_8));
            var s = h.encode();
            var nw = ScxHttpHeaders.of(s);
        }
        System.out.println((System.nanoTime() - l) / 1000_000);
    }

}
