# Kaze: Java simple web app framework
[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze)

- Minimal dependencies (only Servlet API)
- Supports embedded Jetty
- Also runs in other servlet containers
- Easy to create RESTful API, Web API, etc


## Hello World Example (Using embedded Jetty)
### 1. Add dependency
```xml
    <dependency>
      <groupId>com.github.mamorum</groupId>
      <artifactId>kaze</artifactId>
      <version>0.2.4</version>
    </dependency>
    <dependency>
      <groupId>org.eclipse.jetty</groupId>
      <artifactId>jetty-servlet</artifactId>
      <version>9.4.6.v20170531</version>
    </dependency>
```

### 2. Create code
```java
package kaze.sample.html;

import kaze.App;
import kaze.server.Jetty;

// To check:
//  app -> http://localhost:8080/app/hello
//  doc -> http://localhost:8080/ (or /index.html)
public class JettyApp {
  public static void main(String[] args) {
    App app = new App();
    app.get("/hello", (req, res) -> {
      res.html("<p>Hello World from Jetty.</p>");
    });
    Jetty.app(app, "/app/*");
    Jetty.doc("/public", "/");
    Jetty.listen(8080);
  }
}
```

### 3. Run
```
$ mvn compile
$ mvn exec:java -Dexec.mainClass=kaze.sample.html.JettyApp
```

### 4. Check
```
$ curl -s -X GET http://localhost:8080/app/hello
<p>Hello World from Jetty.</p>
```

## Samples
- [kaze-sample-hw](https://github.com/mamorum/kaze-sample/tree/master/hw): hello world examples, including html and json responses.
- [kaze-sample-rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb): web app accessing relational database, packaged as fatjar.
- [kaze-sample-war](https://github.com/mamorum/kaze-sample/tree/master/war): web app, packaged as war.
