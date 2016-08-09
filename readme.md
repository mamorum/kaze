# Kaze: Java web application framework
[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze)

Kaze makes it easy to create Web API, RESTful API, etc. 


## Hello World example
Following code is [Main.java](https://github.com/mamorum/kaze/blob/master/src/test/java/kaze/it/http/Main.java) in `kaze/src/test/java/kaze/it/http`.

```java
package kaze.it.http;

import static kaze.Http.Method.*;

import kaze.App;
import kaze.Http;
import kaze.http.Req;
import kaze.http.Res;

public class Main {

  @Http({GET, "/hello"})
  public void hello(Req req, Res res) {
    res.json("msg", "Hello World!");
  }

  public static void main(String[] args) {
    App.start("kaze.it.http");
  }
}
```

Run as java application, and it serves on port `8080`.

```txt
$ curl -X GET http://localhost:8080/hello -s
{"msg":"Hello World!"}
```


## Guides
- [Getting Started](doc/getting-started.md)
- User Guide - Creating ...


## Samples
- [kaze-sample-rdb](https://github.com/mamorum/kaze-sample/tree/master/rdb) : web app accessing relational database.


## Features
- Routing
- Serving Static Contents
- Conversion (JSON, Java type)
- Validation
- Simple and Fluent APIs
- Light Weight


## Meaning
Kaze means Wind in Japanese.
