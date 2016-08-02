## Installing
### kaze
Maven

```xml
<dependency>
  <groupId>com.github.mamorum</groupId>
  <artifactId>kaze</artifactId>
  <version>0.0.1</version>
</dependency>
```

Gradle

```gradle
compile 'com.github.mamorum:kaze:0.0.1'
```

### logback-classic
Maven

```xml
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
</dependency>
```

Latest version can be checked on [Central Repository (logback-classic)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22ch.qos.logback%22%20AND%20a%3A%22logback-classic%22) or [Logback Web Site](http://logback.qos.ch/).

Kaze depends on [SLF4J](http://www.slf4j.org/index.html). To output logging, please add logback-classic or other logging implementation to your app. The way to switch logging frameworks is written in the [SLF4J user manual](http://www.slf4j.org/manual.html).


## Hello World

In the package `sample.kaze`, create `Main.java` and save the following code.

```java
package sample.kaze;

import kaze.App;
import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class Main {

  @Http({"GET", "/hello"})
  public void hello(Req req, Res res) {
    res.json("msg", "Hello World!");
  }

  public static void main(String[] args) {
    App.start("sample.kaze");
  }
}
```

Run it as java application, and it serves on port `8080`.

Http request `"GET /hello"` gets JSON response like this.

```txt
$ curl http://localhost:8080/hello -s
{"msg":"Hello World!"}
```


## Routing
Comming Soon ...


## Serving Static Contents
Comming Soon ...

