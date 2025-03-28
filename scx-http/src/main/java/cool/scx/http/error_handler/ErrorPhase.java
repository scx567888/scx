package cool.scx.http.error_handler;

/// 错误阶段
public enum ErrorPhase {

    /// 系统阶段, 比如解析 Http 头时发生错误
    SYSTEM,

    /// 用户阶段, 比如用户代码错误
    USER

}
