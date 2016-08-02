[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)


## Kaze - Java web application framework
Kaze makes it easy to create Web API, RESTful API, etc.


## Meaning
Kaze means Wind in Japanese.


## Features
- Routing
- Serving Static Contents
- JSON Conversion
- Java Type Conversion
- Validation
- Simple and Fluent APIs
- Light Weight


## How to get it ?
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


## Sample Application

```java
package sample.kaze;

import kaze.App;
import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class Main {

  @Http({"GET", "/hello"})
  public void hello(Req req, Res res) {
    res.json("msg", "Hello!");
  }

  public static void main(String[] args) {
    App.start("sample.kaze");
  }
}
```

Run as java application, and serves ...

```
$ curl http://localhost:8080/hello -s
{"msg":"Hello!"}
```


## Details
- [Kaze - User Guide](user-guide.md)
