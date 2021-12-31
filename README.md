<p align="center">
    <img src="https://scx.cool/img/scx-logo.svg" width="300px"  alt="scx-logo"/>
</p>
<p align="center">
    <a target="_blank" href="https://github.com/scx567888/scx/actions/workflows/ci.yml">
        <img src="https://github.com/scx567888/scx/actions/workflows/ci.yml/badge.svg" alt="CI"/>
    </a>
    <a target="_blank" href="https://search.maven.org/artifact/cool.scx/scx">
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
    <a target="_blank" href="https://github.com/eclipse-vertx/vert.x">
        <img src="https://img.shields.io/badge/Vert.x-f44336" alt="Vert.x"/>
    </a>
    <a target="_blank" href="https://github.com/FasterXML/jackson">
        <img src="https://img.shields.io/badge/Jackson-ff8000" alt="Jackson"/>
    </a>
    <a target="_blank" href="https://github.com/spring-projects/spring-framework">
        <img src="https://img.shields.io/badge/Spring Framework-d8b125" alt="Spring Framework"/>
    </a>
    <a target="_blank" href="https://github.com/brettwooldridge/HikariCP">
        <img src="https://img.shields.io/badge/HikariCP-98c510" alt="HikariCP"/>
    </a>
    <a target="_blank" href="https://github.com/apache/freemarker">
        <img src="https://img.shields.io/badge/FreeMarker-44be16" alt="FreeMarker"/>
    </a>
    <a target="_blank" href="https://github.com/bcgit/bc-java">
        <img src="https://img.shields.io/badge/Bouncy Castle-37b484" alt="Bouncy Castle"/>
    </a>
    <a target="_blank" href="https://github.com/jasypt/jasypt">
        <img src="https://img.shields.io/badge/Jasypt-29aaf5" alt="Jasypt"/>
    </a>
    <a target="_blank" href="https://github.com/coobird/thumbnailator">
        <img src="https://img.shields.io/badge/Thumbnailator-6269d3" alt="Thumbnailator"/>
    </a>
    <a target="_blank" href="https://github.com/mysql/mysql-connector-j">
        <img src="https://img.shields.io/badge/MySQL Connector/J-9c27b0" alt="MySQL Connector/J"/>
    </a>
    <br/>
    <a target="_blank" href="https://github.com/qos-ch/slf4j">
        <img src="https://img.shields.io/badge/SLF4J-f44336" alt="SLF4J"/>
    </a>
    <a target="_blank" href="https://github.com/apache/logging-log4j2">
        <img src="https://img.shields.io/badge/Apache Log4j 2-44be16" alt="Apache Log4j 2"/>
    </a> 
    <a target="_blank" href="https://github.com/cbeust/testng">
        <img src="https://img.shields.io/badge/TestNG-9c27b0" alt="TestNG"/>
    </a>
</p>

> 一个 Web 后台快速开发框架

## 快速开始

#### 1. 导入依赖 。

``` xml
<dependency>
    <groupId>cool.scx</groupId>
    <artifactId>scx</artifactId>
    <version>{version}</version>
</dependency>
```

#### 2. 编写您自己的模块并运行 main 方法 。

``` java
//注意 : 自定义的模块需要继承 ScxModule
public class YourModule implements ScxModule {

    public static void main(String[] args) {
        // 使用 Scx 构建器 ,构建并运行 项目
        Scx.builder()
                .setMainClass(YourModule.class) //1, Main 方法的 Class
                .addModule(new YourModule())    //2, 您自己的模块
                .setArgs(args)                  //3, 外部参数
                .build().run();                 //4, 构建并运行项目
    }

}
```

更多信息请查看 [文档 | DOCS](https://scx.cool/docs/scx/index.html)

备注 : 我还提供了一些拓展模块方便使用 具体请看 [SCX-EXT](https://github.com/scx567888/scx-ext)
