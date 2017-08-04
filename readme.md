# Kaze: Java simple web app framework
[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze)

- Minimal dependencies (only Servlet API)
- No dependency injection (no DI)
- No annotation and configuration file
- Supports embedded server (Jetty)
- Also runs in a servlet container
- Easy to create RESTful API, Web API, etc


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
package demo.jetty;

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
$ mvn exec:java -Dexec.mainClass=demo.jetty.Main
```

### 4. Check
```
$ curl -s -X GET http://localhost:8080/
<p>Hello World</p>
```

### Note
Above code is [src/test/java/demo/jetty/Main.java](src/test/java/demo/jetty/Main.java).  
We can also run the example using following commands.

```
$ git clone https://github.com/mamorum/kaze.git
$ cd kaze
$ mvn test-compile
$ mvn exec:java -Dexec.mainClass=demo.jetty.Main -Dexec.classpathScope=test
```


## Samples
- [kaze-sample-rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb): fatjar web app, accessing relational database
- [kaze-sample-war](https://github.com/mamorum/kaze-sample/tree/master/server): war app, running in a servlet container


## Meaning
Kaze means Wind in Japanese.
