# Kaze: Java web application framework
[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze)

Kaze makes it easy to create Web API, RESTful API, etc. 


## Hello World Example
### 1. Add dependency
```xml
<dependency>
  <groupId>com.github.mamorum</groupId>
  <artifactId>kaze</artifactId>
  <version>0.2.2</version>
</dependency>
```

### 2. Create code
```java
package demo;

import kaze.App;
import kaze.server.Jetty;

public class Main {
  public static void main(String[] args) {
    App app = new App();
    app.get("/", (req, res) -> {
      res.json("msg", "Hello!");
    });
    Jetty jetty = new Jetty(app);
    jetty.listen(8080);
  }
}
```

### 3. Run
```
mvn exec:java -Dexec.mainClass=demo.Main
```

### 4. Check
```
curl -s -X GET http://localhost:8080/
{"msg":"Hello!"}
```


## Samples
- [kaze-sample-rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb) : web app accessing relational database.


## Meaning
Kaze means Wind in Japanese.
