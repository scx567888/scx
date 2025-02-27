package cool.scx.tcp.tls;

/// 存储解析出来的 PEM 文件内容
///
/// @param content PEM 中的二进制数据
/// @param marker  PEM 的标记，例如 "CERTIFICATE"
public record PemObject(byte[] content, String marker) {

}
