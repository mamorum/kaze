# Kaze: Java simple web app framework
[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze)

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
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.8.2</version>
</dependency>
```

### 2. Create code
```java
package kaze.sample.gs.json;

import com.google.gson.Gson;

import kaze.App;
import kaze.server.Jetty;

// app -> http://localhost:8080/app/msg
// doc -> http://localhost:8080/*
public class GsonMain {
  static final Gson gson = new Gson();
  public static void main(String[] args) {
    App app = new App();
    app.conv(gson::fromJson, gson::toJson);
    app.get("/msg", (req, res) -> {
      res.json("msg", "Hello World.");
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
gs> mvn exec:java -Dexec.mainClass=kaze.sample.gs.json.GsonMain
```

### 4. Check
```
> curl -s -X GET http://localhost:8080/app/msg
{"msg":"Hello, World."}
```


## Examples
- [gs](https://github.com/mamorum/kaze-sample/tree/master/gs): above hello world and basic examples.
- [rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb): web app accessing relational database, packaged as fatjar.
- [war](https://github.com/mamorum/kaze-sample/tree/master/war): web app for servlet container (tomcat, etc).

