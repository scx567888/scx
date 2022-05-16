package cool.scx.util;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * 基本网络操作工具类
 *
 * @author scx567888
 * @version 0.3.6
 */
public final class NetUtils {

    /**
     * 获取访问者IP
     * <p>
     * 先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用 Request .remoteAddress()
     *
     * @param context a {@link io.vertx.ext.web.RoutingContext} object
     * @return IP
     */
    public static String getIpAddress(RoutingContext context) {
        if (context == null) {
            return "";
        }
        HttpServerRequest request = context.request();
        var ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            if (ip.contains("../") || ip.contains("..\\")) {
                return "";
            }
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                ip = ip.substring(0, index);
            }
            if (ip.contains("../") || ip.contains("..\\")) {
                return "";
            }
        } else {
            ip = request.remoteAddress().host();
            if (ip.contains("../") || ip.contains("..\\")) {
                return "";
            }
            if (ip.equals("0:0:0:0:0:0:0:1")) {
                ip = "127.0.0.1";
            }
        }
        return ip;
    }

    /**
     * 获取本机的 IP 地址
     *
     * @return 本机的 IP 地址
     */
    public static IPAddress getLocalAddress() {
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
            Collections.addAll(l, v4);
            Collections.addAll(l, v6);
            return "[" + String.join(", ", l) + "]";
        }

    }

}
