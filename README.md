<p align="center">
    <img src="https://scx.cool/logos/scx-logo.svg" width="300px"  alt="scx-logo"/>
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
        <img src="https://img.shields.io/badge/HikariCP-44be16" alt="HikariCP"/>
    </a>
    <a target="_blank" href="https://github.com/apache/freemarker">
        <img src="https://img.shields.io/badge/FreeMarker-29aaf5" alt="FreeMarker"/>
    </a>
    <a target="_blank" href="https://github.com/coobird/thumbnailator">
        <img src="https://img.shields.io/badge/Thumbnailator-9c27b0" alt="Thumbnailator"/>
    </a>
    <br/>
    <a target="_blank" href="https://github.com/jasypt/jasypt">
        <img src="https://img.shields.io/badge/Jasypt-f44336" alt="Jasypt"/>
    </a>
    <a target="_blank" href="https://github.com/mysql/mysql-connector-j">
        <img src="https://img.shields.io/badge/MySQL Connector/J-ff8000" alt="MySQL Connector/J"/>
    </a>
    <a target="_blank" href="https://github.com/java-native-access/jna">
        <img src="https://img.shields.io/badge/JNA-d8b125" alt="Java Native Access (JNA)"/>
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

English | [简体中文](./README.zh-CN.md)

> A Web rapid development framework

## Maven

``` xml
<dependency>
    <groupId>cool.scx</groupId>
    <artifactId>scx-core</artifactId>
    <version>{version}</version>
</dependency>
```

## Quick start

#### 1. Write your own module and run the main method .

``` java
import cool.scx.common.standard.HttpMethod;
import cool.scx.core.Scx;
import cool.scx.core.ScxModule;
import cool.scx.web.annotation.ScxRoute;

// Note : Custom modules need extends ScxModule
// This @ScxRoute indicate this class needs to be scanned by WebHandler
@ScxRoute
public class YourModule extends ScxModule {

    public static void main(String[] args) {
        // Use Scx Builder, build and run project
        Scx.builder()
                .setMainClass(YourModule.class) // 1, The class of the Main method
                .addModule(new YourModule())    // 2, Your own modules
                .setArgs(args)                  // 3, External parameters
                .run();                         // 4, Build and run project
    }

    // This @ScxRoute indicate this method is a WebHandler 
    // The path is "" and the request method is GET
    @ScxRoute(value = "", methods = HttpMethod.GET)
    public String helloWorld() {
        // The content that will be sent to the client 
        return "Hello World";
    }

}
```

#### 2. Use your browser to access http://localhost:8080/ , you should see this .

```html
Hello World
```

For more information, see [docs](https://scx.cool/docs/scx/index.html)

## Stats

![Alt](https://repobeats.axiom.co/api/embed/7c4eddb6eff53274d58005e1fbe519b0807cbce3.svg "Repobeats analytics image")
