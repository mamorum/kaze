# Kaze: Java web application framework
[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze)

- Minimal framework classes and APIs
- Minimal dependencies (only Servlet API)
- Supports embedded server (Jetty)
- Also runs in a servlet container
- Easy to create Web API, RESTful API, etc


## Hello World Example (Using embedded Jetty)
### 1. Add dependency
```xml
    <dependency>
      <groupId>com.github.mamorum</groupId>
      <artifactId>kaze</artifactId>
      <version>0.2.2</version>
    </dependency>
    <dependency>
      <groupId>org.eclipse.jetty</groupId>
      <artifactId>jetty-server</artifactId>
      <version>9.4.6.v20170531</version>
    </dependency>
```

### 2. Create code
```java
package kaze.sample.server;

import kaze.App;
import kaze.server.Jetty;

public class Main {
  public static void main(String[] args) {
    App.get("/", (req, res) -> {
      res.send("Hello World");
    });
    Jetty.listen(8080);
  }
}
```

### 3. Run
```
mvn exec:java -Dexec.mainClass=kaze.sample.server.jetty.Main
```

### 4. Check
```
curl -s -X GET http://localhost:8080/
Hello World
```


## Samples
- [kaze-sample-server](https://github.com/mamorum/kaze-sample/tree/master/server): Using embedded server.
- [kaze-sample-rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb): Web app accessing relational database.


## Meaning
Kaze means Wind in Japanese.
