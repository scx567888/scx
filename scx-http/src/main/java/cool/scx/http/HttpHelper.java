package cool.scx.http;

import static cool.scx.common.util.StringUtils.notBlank;

public class HttpHelper {

    /**
     * 获取访问者IP
     * <p>
     * 先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用 HttpServerRequest.remoteAddress()
     *
     * @param request a
     * @return IP
     */
    public static String getRequestIP(ScxHttpServerRequest request) {

        var xRealIPStr = request.getHeader("X-Real-IP");
        if (notBlank(xRealIPStr) && !"unknown".equalsIgnoreCase(xRealIPStr)) {
            return xRealIPStr;
        }

        var xForwardedForStr = request.getHeader("X-Forwarded-For");
        if (notBlank(xForwardedForStr) && !"unknown".equalsIgnoreCase(xForwardedForStr)) {
            var s = xForwardedForStr.split(",");
            if (s.length > 0) {
                return s[0];
            }
        }

        var remoteAddressStr = request.remotePeer().address().toString();
        if ("0:0:0:0:0:0:0:1".equals(remoteAddressStr)) {
            return "127.0.0.1";
        } else {
            return remoteAddressStr;
        }
        
    }

}
