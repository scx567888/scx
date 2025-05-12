此教程旨在帮助用户快速了解此框架
让我们先从一个 简单的 http 服务器写起
首先引入 scx-http-x 依赖

```xml
<dependency>
    <groupId>cool.scx</groupId>
    <artifactId>scx-http-x</artifactId>
    <version>{version}</version>
</dependency>
```

然后编写如下代码

```java
var httpServer = new HttpServer();
httpServer.onRequest(request -> {
    request.response().send("Hello World");
});
httpServer.start(8899);
```

运行访问 http://localhost:8899 即可 实现简单的 http 服务器
