## 配置设计

## 1. 配置类型：通信系统的语义构成单位

在设计中，**配置是一种宏观的语义概念**，用于表达通信系统中各类可设定行为。我们将其划分为三类，每类配置对应不同的生命周期与职责。

### 📚 三类配置

| 配置类型       | 含义与职责            | 生命周期        | 示例                                |
|------------|------------------|-------------|-----------------------------------|
| **主体配置**   | 定义客户端或服务器本体的长期特性 | 与通信对象生命周期一致 | `tls`、`backlog`                   |
| **连接行为配置** | 控制连接建立过程的短时策略    | 仅在连接阶段存在    | `timeout`、`bindPort`、`remoteAddr` |
| **事件注册配置** | 描述连接建立后对事件的响应策略  | 与连接生命周期一致   | `onConnect(handler)`、`onClose()`  |

### 🧩 为何显式分类？

> 尽管**这三类配置可以通过构造函数、类方法或 setter 设置**，我们依然选择在语义上进行显式划分，原因在于：

- **突出各配置类型的职责边界与生命周期差异**；
- **提升使用者的理解能力与使用意图的清晰表达**；
- **遵循接口一致性与纯粹性设计原则**。

这不是实现上的限制，而是设计层次上的选择。

---

### 📦 对应的传递方式（设计约定）

| 配置类型   | 默认传递方式                     | 说明                                        |
|--------|----------------------------|-------------------------------------------|
| 主体配置   | 构造参数 / Setter              | 生命周期长、可动态修改，通常通过 `options()` 获取和设置        |
| 连接行为配置 | `start()` / `connect()` 参数 | 仅在连接建立阶段传递，随后即失效                          |
| 事件注册配置 | 独立方法注册                     | 虽为“配置”，但为了突出其语义，采用显式注册形式（如 `onConnect()`） |

---

## ✅ 使用示例

```java
public static void serverDemo() {

    // 通信主体持有配置
    var server = new TCPServer(new TCPServerOptions().tls(TLS.ofDefault()));

    // 可随时动态修改配置
    server.options().tls(TLS.ofTrustAny());

    // 事件注册配置
    server.onConnect(connect -> {
        System.out.println(connect.remoteAddress());
    });

    // 连接行为配置
    server.start(8899);
}

public static void clientDemo() {

    // 通信主体持有配置
    var client = new TCPClient(new TCPClientOptions().tls(TLS.ofDefault()));

    // 可随时动态修改配置
    client.options().tls(TLS.ofTrustAny());

    // client 不支持事件注册

    // 连接行为配置
    client.connect(new InetSocketAddress("127.0.0.1", 8899), 5000);
}
```
