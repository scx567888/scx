# SCX TCP 框架设计思路

## 1. 统一性：连续性的存在

### 🌟 一切通信皆源于「连续性的存在」

从最基础的哲学角度来看，通信世界中的所有交互都可以被视为某种“可重复发生的连续存在”。  
不论是 TCP 连接与数据传输，HTTP 请求与响应，还是 WebSocket 消息，它们本质上都是一种持续的信息交换过程。  
这个过程是动态的、连续的，可以从不同抽象层次进行划分与理解。

---

## 2. 抽象视角：事件与流

我们从两个核心视角来观察这种连续性：**事件** 与 **流**。

### ✴️ 两种视角

| 类型            | 定义         | 特征        | 示例                            |
|---------------|------------|-----------|-------------------------------|
| **事件（Event）** | 瞬时发生的离散动作  | 不可预测、不可持续 | TCP 连接建立、HTTP 请求到达            |
| **流（Stream）** | 可持续消费的数据序列 | 有起点、可控制读取 | TCP 数据内容、HTTP 请求体、WebSocket 帧 |

- **事件**：代表某一瞬间的不确定触发点，是信息流的“切分点”，只可响应，无法预测。
- **流**：代表可控的、持续的信息传输，可按需主动读取，是“连续性存在”的本体。

> ✨ 事件是点，流是线。

---

## 3. 事件与流的相互关系

事件与流并非对立，而是同一事物的两个抽象层级。

- **事件是流的切分点**，用于表达某一瞬时行为的边界；
- **流是事件的展开体**，代表持续的数据传输过程；
- 两者可相互转化，视语义而定。

在 SCX 的设计中，清晰地区分事件与流，有助于明确责任边界、保持设计纯洁性并增强开发者的理解力。

---

## 4. 消费方式上的差异：Push vs Pull

| 机制     | 描述      | 行为模式         |
|--------|---------|--------------|
| **事件** | 系统主动推送  | 回调（callback） |
| **流**  | 使用者主动拉取 | 读取（read）接口   |

### 示例对照

| 场景             | 类型 | 接口                      |
|----------------|----|-------------------------|
| 新的 TCP 连接建立    | 事件 | `onConnect(handler)`    |
| 接收 TCP 数据      | 流  | `socket.read()`         |
| 新的 HTTP 请求建立   | 事件 | `onRequest(handler)`    |
| 接受 WebSocket 帧 | 流  | `websocket.readFrame()` |

> ✅ 未来所有通信协议（TCP/HTTP/WebSocket）皆遵循此设计思想，确保全框架设计的**一致性与可组合性**。

---

## 5. 参数设计：构造参数 vs 启动参数

在 SCX 中，所有配置可分为三类：

| 类型   | 定义                           | 示例                                |
|------|------------------------------|-----------------------------------|
| 自有配置 | 表示“连接成功之后的行为配置”，生命周期长、行为确定   | `tls`、`backlog`                   |
| 启动配置 | 表示“连接建立过程中的行为”，生命周期短，建立完成即失效 | `timeout`、`bindPort`、`remoteAddr` |
| 事件注册 | 表示连接建立后的事件监听行为               | `onConnect`                       |

虽然理论上所有配置都可以统一为通过构造函数或 setter 完成，但 SCX 有意识地进行区分：

- **自有配置**：在对象生命周期内持续生效，控制连接后的行为；我们通过 构造函数传递 并在类实例中通过 options 的方式暴漏出来 外界可以随时修改
- **启动配置**：传递给 `start()` 或 `connect()`，只在连接创建阶段有效；我们通过方法本身传递参数 
- **事件注册**：理论上也属于可以放置在构造参数中，但由于事件语义的重要性，框架选择将其以独立方式暴露出来（例如 `server.onConnect()`）。

### 举例：

```java
import cool.scx.tcp.TCPClient;
import cool.scx.tcp.TCPClientOptions;
import cool.scx.tcp.TCPServer;
import cool.scx.tcp.TCPServerOptions;
import cool.scx.tcp.tls.TLS;

import java.net.InetSocketAddress;

public static void serverDemo() {

    // 通过构造参数传递
    var server = new TCPServer(new TCPServerOptions().tls(TLS.ofDefault()));

    // 可以随时修改
    server.options().tls(TLS.ofTrustAny());

    // 事件配置
    server.onConnect(connect -> {
        System.out.println(connect.remoteAddress());
    });

    // 启动参数
    server.start(8899);
}

public static void clientDemo() {

    // 通过构造参数传递
    var server = new TCPClient(new TCPClientOptions().tls(TLS.ofDefault()));

    // 可以随时修改
    server.options().tls(TLS.ofTrustAny());

    // 事件配置 client 没有事件概念

    // 连接参数
    server.connect(new InetSocketAddress("127.0.0.1",8899),5000);
}

```

---

## 🧠 结语

SCX 的设计并不依赖主流共识，而是以哲学抽象为基础，构建出一致、纯粹、可验证、可组合的通信模型。  
所有的概念都源自于对“连续性存在”的理解，并在事件与流的统一视角下进行组织和实现。

> 如果你欣赏逻辑的力量、抽象的简洁、接口的一致性，那么 SCX 会是你值得信赖的通信基础设施。

---
