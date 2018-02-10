# Kaze: Java simple web app framework
[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze)

- Easy to create RESTful API, Web API, etc
- Runs on servlet containers
- Minimal dependencies (Only Servlet API)
- Supports embedded Jetty (Optional)


## Hello World Example
This example is in [kaze-samples/hw](https://github.com/mamorum/kaze-sample/tree/master/hw).

### 1. Add dependencies
```xml
    <dependency>
      <groupId>com.github.mamorum</groupId>
      <artifactId>kaze</artifactId>
      <version>0.2.6</version>
    </dependency>
    <dependency>
      <groupId>org.eclipse.jetty</groupId>
      <artifactId>jetty-servlet</artifactId>
      <version>9.4.6.v20170531</version>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.8.2</version>
    </dependency>
```

### 2. Create code
```java
package kaze.sample.hw;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

// To check:
//  app -> http://localhost:8080/app/hello
//  doc -> http://localhost:8080/ (or /index.html)
public class Main {
  public static void main(String[] args) {
    App app = new App();
    Gson gson = new Gson();
    app.conv(gson::fromJson, gson::toJson);
    app.get("/hello", (req, res) -> {
      res.json("msg", "Hello, World.");
    });
    Jetty.app(app, "/app/*");
    Jetty.doc("/public", "/");
    Jetty.listen(8080);
  }
}
```

### 3. Run
```
hw> mvn compile
hw> mvn exec:java -Dexec.mainClass=kaze.sample.hw.Main
```

### 4. Check
```
> curl -s -X GET http://localhost:8080/app/hello
{"msg":"Hello, World."}
```


## Other Samples
- [war](https://github.com/mamorum/kaze-sample/tree/master/war): depending only on servlet api, packaged as war.
- [rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb): accessing relational database, packaged as fatjar.
- [tomcat](https://github.com/mamorum/kaze-sample/tree/master/tomcat): using embedded tomcat.
- [jackson](https://github.com/mamorum/kaze-sample/tree/master/jackson): using [jackson](https://github.com/FasterXML/jackson) as a json converter.
