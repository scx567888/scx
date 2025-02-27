package cool.scx.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.function.Predicate;

import static java.net.NetworkInterface.networkInterfaces;

/// 基本网络操作工具类
///
/// @author scx567888
/// @version 0.0.1
public final class NetUtils {

    /// 获取本机的 IP 地址 (不包括回环地址)
    ///
    /// @return 本机的 IP 地址
    public static InetAddress[] getLocalIPAddress(Predicate<InetAddress> filter) throws SocketException {
        return networkInterfaces().flatMap(NetworkInterface::inetAddresses).filter(c -> !c.isLoopbackAddress() && filter.test(c)).toArray(InetAddress[]::new);
    }

    public static InetAddress[] getLocalIPAddress() throws SocketException {
        return getLocalIPAddress(c -> true);
    }

}
