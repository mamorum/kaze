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
      <version>0.2.3</version>
    </dependency>
    <dependency>
      <groupId>org.eclipse.jetty</groupId>
      <artifactId>jetty-servlet</artifactId>
      <version>9.4.6.v20170531</version>
    </dependency>
```

### 2. Create code
```java
package kaze.sample.hw;

import kaze.App;
import kaze.server.Jetty;

public class Main {
  public static void main(String[] args) {
    App.get("/", (req, res) -> {
      res.html("<p>Hello World</p>");
    });
    Jetty.listen(8080);
  }
}
```

### 3. Run
```
$ mvn compile
$ mvn exec:java -Dexec.mainClass=kaze.sample.hw.Main
```

### 4. Check
```
$ curl -s -X GET http://localhost:8080/
<p>Hello World</p>
```

## Samples
- [kaze-sample-hw]((https://github.com/mamorum/kaze-sample/tree/master/hw): Above Hello World Example.
- [kaze-sample-rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb): web app accessing relational database, packaged as fatjar.
- [kaze-sample-war](https://github.com/mamorum/kaze-sample/tree/master/server): web app, packaged as war.
