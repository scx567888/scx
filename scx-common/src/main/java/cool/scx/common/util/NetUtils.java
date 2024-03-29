package cool.scx.common.util;

import io.vertx.core.http.HttpServerRequest;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.stream.Stream;

import static java.util.Collections.addAll;

/**
 * 基本网络操作工具类
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class NetUtils {

    /**
     * 获取访问者IP
     * <p>
     * 先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用 HttpServerRequest.remoteAddress()
     *
     * @param request a
     * @return IP
     */
    public static String getClientIPAddress(HttpServerRequest request) {
        var xRealIPStr = request.getHeader("X-Real-IP");
        var xForwardedForStr = request.getHeader("X-Forwarded-For");
        var remoteAddressStr = request.remoteAddress().hostAddress();
        var xRealIP = "";
        var xForwardedFor = new String[]{};
        var remoteAddress = "";
        if (StringUtils.notBlank(xRealIPStr) && !"unknown".equalsIgnoreCase(xRealIPStr)) {
            xRealIP = xRealIPStr;
        }
        if (StringUtils.notBlank(xForwardedForStr) && !"unknown".equalsIgnoreCase(xForwardedForStr)) {
            xForwardedFor = xForwardedForStr.split(",");
        }
        if ("0:0:0:0:0:0:0:1".equals(remoteAddressStr)) {
            remoteAddress = "127.0.0.1";
        } else {
            remoteAddress = remoteAddressStr;
        }
        if (StringUtils.notBlank(xRealIP)) {
            return xRealIP;
        } else if (xForwardedFor.length > 0) {
            return xForwardedFor[0];
        } else {
            return remoteAddress;
        }
    }

    /**
     * 获取本机的 IP 地址
     *
     * @return 本机的 IP 地址
     */
    public static IPAddress getLocalIPAddress() {
        try {
            //所有非回环的地址
            var allAddresses = Stream.concat(NetworkInterface.networkInterfaces().flatMap(NetworkInterface::inetAddresses), Stream.of(InetAddress.getLocalHost())).filter(c -> !c.isLoopbackAddress()).toList();
            var ipv4AddressList = allAddresses.stream().filter(c -> c instanceof Inet4Address).map(InetAddress::getHostAddress).distinct().toArray(String[]::new);
            var ipv6AddressList = allAddresses.stream().filter(c -> c instanceof Inet6Address).map(InetAddress::getHostAddress).distinct().toArray(String[]::new);
            return new IPAddress(ipv4AddressList, ipv6AddressList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new IPAddress(new String[0], new String[0]);
    }

    /**
     * IP 地址包装类
     *
     * @param v4 所有 ipv4 地址
     * @param v6 所有 ipv6 地址
     */
    public record IPAddress(String[] v4, String[] v6) {

        /**
         * 当 ipv4 地址列表不为空时返回 ipv4 列表 否则返回 ipv6 地址
         *
         * @return 标准的 IP
         */
        public String[] getNormalIP() {
            return v4.length > 0 ? v4 : v6;
        }

        /**
         * 所有 ipv4 地址转字符串 (一般用于打印或日志记录使用)
         *
         * @return 字符串
         */
        public String v4ToString() {
            return "[" + String.join(", ", v4) + "]";
        }

        /**
         * 所有 ipv6 地址转字符串 (一般用于打印或日志记录使用)
         *
         * @return 字符串
         */
        public String v6ToString() {
            return "[" + String.join(", ", v6) + "]";
        }

        /**
         * 将所有的 ipv4 和 ipv6 地址转换为字符串 ipv4 在前, ipv6 在后, (一般用于打印或日志记录使用)
         *
         * @return 字符串
         */
        @Override
        public String toString() {
            var l = new ArrayList<String>();
            addAll(l, v4);
            addAll(l, v6);
            return "[" + String.join(", ", l) + "]";
        }

    }

}
