## What is this ?
Java Web Application Framework.

Kaze means Wind in japanese.


## Features
- Simple and Fluent APIs
- Light Weight
- Routing
- JSON Conversion
- Java Type Conversion
- Validation
- Serving Static Contents


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
