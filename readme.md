## What is this ?
Simple Web Application Framework in Java.


## Kaze = Wind
Kaze means wind in japanese.（日本語表記：風・かぜ）


## Status
Developping. 


## Sample Application

```
package kaze.test.http;

import kaze.App;
import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class Main {

  @Http({"GET", "/"})
  public void index(Req req, Res res) {
    res.json("msg", "Hello!");
  }
  
  public static void main(String[] args) {
    App.start("kaze.test.http");
  }
}
```

Run as java application, and serves ...

```
$ curl http://localhost:8080/ -s
{"msg":"Hello!"}
```

