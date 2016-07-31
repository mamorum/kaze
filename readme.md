## Kaze - Java Web Application Framework
Makes it easy to create Web API, RESTful API, etc.

Kaze means Wind in Japanese.

[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)


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

```
Comming soon...
```

Gradle

```
Comming soon...
```


## Sample Application

```java
package kaze.sample;

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
    App.start("kaze.sample");
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
