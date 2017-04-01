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
  <version>0.2.0</version>
</dependency>
```

### 2. Create code
```java
package demo;

import kaze.Http;

public class Main {
  public static void main(String[] args) {
    Http.server().get("/hello", (req, res) -> {
      res.json("msg", "Hello World!");
    }).listen();
  }
}
```

### 3. Run
```
mvn exec:java -Dexec.mainClass=demo.Main
```

### 4. Check
```
$ curl -X GET http://localhost:8080/hello -s
{"msg":"Hello World!"}
```


## Guides
- [Getting Started](doc/getting-started.md)
- [User Guide](doc/user-guide.md)


## Samples
- [kaze-sample-rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb) : web app accessing relational database.


## Features
- Routing
- Serving Static Contents
- Conversion (JSON, Java types)
- Validation
- Simple and Fluent APIs
- Light Weight


## Meaning
Kaze means Wind in Japanese.
