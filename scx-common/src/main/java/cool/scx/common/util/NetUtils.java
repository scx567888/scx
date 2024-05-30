package cool.scx.common.util;

import io.vertx.core.http.HttpServerRequest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.function.Predicate;

import static cool.scx.common.util.StringUtils.notBlank;
import static java.net.NetworkInterface.networkInterfaces;

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

        var remoteAddressStr = request.remoteAddress().hostAddress();
        if ("0:0:0:0:0:0:0:1".equals(remoteAddressStr)) {
            return "127.0.0.1";
        } else {
            return remoteAddressStr;
        }

    }

    /**
     * 获取本机的 IP 地址 (不包括回环地址)
     *
     * @return 本机的 IP 地址
     */
    public static InetAddress[] getLocalIPAddress(Predicate<InetAddress> filter) throws SocketException {
        return networkInterfaces().flatMap(NetworkInterface::inetAddresses).filter(c -> !c.isLoopbackAddress() && filter.test(c)).toArray(InetAddress[]::new);
    }

    public static InetAddress[] getLocalIPAddress() throws SocketException {
        return getLocalIPAddress(c -> true);
    }

}
