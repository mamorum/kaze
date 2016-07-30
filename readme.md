## What is this ?
Java Web Application Framework.

Kaze makes it easy to create Web API, RESTful API, and so on.


## Meaning of Kaze
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
