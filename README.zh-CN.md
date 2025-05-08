<p align="center">
    <img src="https://scx.cool/scx-logo/scx-logo.svg" width="300px"  alt="scx-logo"/>
</p>
<p align="center">
    <a target="_blank" href="https://github.com/scx567888/scx/actions/workflows/ci.yml">
        <img src="https://github.com/scx567888/scx/actions/workflows/ci.yml/badge.svg" alt="CI"/>
    </a>
    <a target="_blank" href="https://central.sonatype.com/artifact/cool.scx/scx">
        <img src="https://img.shields.io/maven-central/v/cool.scx/scx?color=ff69b4" alt="maven-central"/>
    </a>
    <a target="_blank" href="https://github.com/scx567888/scx">
        <img src="https://img.shields.io/github/languages/code-size/scx567888/scx?color=orange" alt="code-size"/>
    </a>
    <a target="_blank" href="https://github.com/scx567888/scx/issues">
        <img src="https://img.shields.io/github/issues/scx567888/scx" alt="issues"/>
    </a>
    <a target="_blank" href="https://github.com/scx567888/scx/blob/master/LICENSE">
        <img src="https://img.shields.io/github/license/scx567888/scx" alt="license"/>
    </a>
</p>
<p align="center">
    <a target="_blank" href="https://github.com/FasterXML/jackson">
        <img src="https://img.shields.io/badge/Jackson-f44336" alt="Jackson"/>
    </a>
    <a target="_blank" href="https://github.com/brettwooldridge/HikariCP">
        <img src="https://img.shields.io/badge/HikariCP-ff8000" alt="HikariCP"/>
    </a>
    <a target="_blank" href="https://github.com/apache/freemarker">
        <img src="https://img.shields.io/badge/FreeMarker-44be16" alt="FreeMarker"/>
    </a>
    <a target="_blank" href="https://github.com/coobird/thumbnailator">
        <img src="https://img.shields.io/badge/Thumbnailator-29aaf5" alt="Thumbnailator"/>
    </a>
    <a target="_blank" href="https://github.com/jasypt/jasypt">
        <img src="https://img.shields.io/badge/Jasypt-9c27b0" alt="Jasypt"/>
    </a>
    <br/>
    <a target="_blank" href="https://github.com/jmrozanec/cron-utils">
        <img src="https://img.shields.io/badge/Cron utils-f44336" alt="Cron utils"/>
    </a>
    <a target="_blank" href="https://github.com/mysql/mysql-connector-j">
        <img src="https://img.shields.io/badge/MySQL Connector/J-ff8000" alt="MySQL Connector/J"/>
    </a>
    <a target="_blank" href="https://github.com/qos-ch/slf4j">
        <img src="https://img.shields.io/badge/SLF4J-44be16" alt="SLF4J"/>
    </a>
    <a target="_blank" href="https://github.com/apache/logging-log4j2">
        <img src="https://img.shields.io/badge/Apache Log4j 2-29aaf5" alt="Apache Log4j 2"/>
    </a>
    <a target="_blank" href="https://github.com/cbeust/testng">
        <img src="https://img.shields.io/badge/TestNG-9c27b0" alt="TestNG"/>
    </a>
</p>

简体中文 | [English](./README.md)

> 一个简单的 Web 开发框架

## Maven

``` xml
<dependency>
    <groupId>cool.scx</groupId>
    <artifactId>scx-app</artifactId>
    <version>{version}</version>
</dependency>
```

## 快速开始

#### 1. 编写您自己的模块并运行 main 方法 。

``` java
import cool.scx.app.ScxApp;
import cool.scx.app.ScxAppModule;
import cool.scx.http.method.HttpMethod;
import cool.scx.web.annotation.ScxRoute;

// 注意 : 自定义的模块需要继承 ScxModule
// 此处的 @ScxRoute 注解用来表示这是一个需要被扫描 WebHandler 的类
@ScxRoute
public class YourModule extends ScxAppModule {

    public static void main(String[] args) {
        // 使用 Scx 构建器 ,构建并运行 项目
        ScxApp.builder()
                .setMainClass(YourModule.class) // 1, Main 方法的 Class
                .addModule(new YourModule())    // 2, 您自己的模块
                .setArgs(args)                  // 3, 外部参数
                .run();                         // 4, 构建并运行项目
    }

    // 此处的 @ScxRoute 注解用来表示这是一个具体的 WebHandler
    // 路径为 "" , 请求方法为 GET
    @ScxRoute(value = "", methods = HttpMethod.GET)
    public String helloWorld() {
        // 向页面返回的具体内容
        return "Hello World";
    }

}
```

#### 2. 使用浏览器访问 http://localhost:8080/ , 您应看到如下内容 。

```html
Hello World
```

有关更多信息，请参阅 [文档](https://scx.cool/scx/)

## Stats

![Alt](https://repobeats.axiom.co/api/embed/7c4eddb6eff53274d58005e1fbe519b0807cbce3.svg "Repobeats analytics image")
