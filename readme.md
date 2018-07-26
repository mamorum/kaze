# Kaze: Java simple web app framework
[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)
<a href="https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze" rel="nofollow"><img src="https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze/badge.svg" alt="Maven Central"></a>

- Easy to create RESTful API, Web API, etc
- Runs on servlet containers
- Supports embedded Jetty (Optional)
- Minimal dependencies (Only Servlet API)


## Hello World Example
### 1. Add dependencies
```xml
<dependency>
  <groupId>com.github.mamorum</groupId>
  <artifactId>kaze</artifactId>
  <version>1.0.0</version>
</dependency>
<dependency>
  <groupId>org.eclipse.jetty</groupId>
  <artifactId>jetty-servlet</artifactId>
  <version>9.4.6.v20170531</version>
</dependency>
```

### 2. Create code
```java
package kaze.sample.gs.html;

import kaze.App;
import kaze.opt.Jetty;

// app -> http://localhost:8080/app/hello
// doc -> http://localhost:8080/*
public class Main {
  public static void main(String[] args) {
    App app = new App();
    app.get("/hello", (req, res) -> {
      res.html("<p>Hello, World.</p>");
    });
    Jetty.app(app, "/app/*");
    Jetty.doc("/public", "/");
    Jetty.listen(8080);
  }
}
```

### 3. Run
```
gs> mvn compile
gs> mvn exec:java -Dexec.mainClass=kaze.sample.gs.html.Main
```

### 4. Check
```
> curl -s -X GET http://localhost:8080/app/hello
<p>Hello, World.</p>
```


## Examples
- [gs](https://github.com/mamorum/kaze-sample/tree/master/gs): basic examples (above and json response).
- [rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb): web app accessing relational database, packaged as fatjar.
- [war](https://github.com/mamorum/kaze-sample/tree/master/war): web app for servlet container, packaged as war.


## Requirements
- Java 8
- Servlet 3.1
- Jetty 9.4.6 (Optional)

And compatible higher versions.
