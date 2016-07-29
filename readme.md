## What is this ?
Simple Web Application Framework in Java. 

Kaze means Wind in japanese. 風 is a Kanji character of kaze.


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

## Features
- Simple and Fluent APIs
- Routing
- Serving Static Contents
- JSON Conversion
- Java Type Conversion
- Validation


## Details
- [Kaze - User Guide](user-guide.md)
