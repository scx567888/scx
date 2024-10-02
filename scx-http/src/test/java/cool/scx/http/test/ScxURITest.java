package cool.scx.http.test;

import cool.scx.http.uri.ScxURI;

import java.util.List;

public class ScxURITest {

    public static void main(String[] args) {
        var s = List.of(
                "http://www.example.com/abc/bcd?name=小明&age=20#999",
                "www.example.com/abc/bcd?name=小明&age=20#999",
                "/abc/bcd?name=小明&age=20#999",
                "abc/bcd?name=小明&age=20#999",
                "路径/bcd?小明=123",
                "路径/bc|d?小明=123",
                "http://www.example.com/ab:c/bc|d?name=小明&age=20#999",
                "http://www.example.com/ab:c/bc|d?name=小明&age=http://www.abc.com",
                "wss://webcast5-ws-web-hl.douyin.com/webcast/im/push/v2/?app_name=douyin_web&version_code=180800&webcast_sdk_version=1.0.14-beta.0&update_version_code=1.0.14-beta.0&compress=gzip&device_platform=web&cookie_enabled=true&screen_width=1280&screen_height=720&browser_language=en-US&browser_platform=Win32&browser_name=Mozilla&browser_version=5.0%20(Windows)&browser_online=true&tz_name=Asia/Shanghai&cursor=t-1727697490940_r-1_d-1_u-1_h-1&internal_ext=internal_src:dim|wss_push_room_id:7420389122457520931|wss_push_did:7420404076984698378|first_req_ms:1727697490898|fetch_time:1727697490940|seq:1|wss_info:0-1727697490940-0-0|wrds_v:7420404212636321638&host=https://live.douyin.com&aid=6383&live_id=1&did_rule=3&endpoint=live_pc&support_wrds=1&user_unique_id=7420404076984698378&im_path=/webcast/im/fetch/&identity=audience&need_persist_msg_count=0&insert_task_id=&live_reason=&room_id=7420389122457520931&heartbeatDuration=0&signature=64qshfDtMjS2vpmj"
        );
        for (String string : s) {
            var uri = ScxURI.of(string);
            var encode = uri.encode();
            System.out.println(encode.equals(string) + " " + encode);
        }
    }

}
