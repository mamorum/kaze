## Kaze: Java web application framework
[![Build Status](https://travis-ci.org/mamorum/kaze.svg?branch=master)](https://travis-ci.org/mamorum/kaze)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mamorum/kaze)

Kaze makes it easy to create Web API, RESTful API, etc. 

To create API, Kaze provides `@Http` method annotaiton. 

```java
@Http({"GET", "/hello"})
public void hello(Req req, Res res) {
  res.json("msg", "Hello World!");
}
// ==> #hello(Req, Res) processes request "GET /hello".
```

Besides `@Http`, there are some features in Kaze.


### Features
- Routing
- Serving Static Contents
- Conversion (JSON, Java type)
- Validation
- Simple and Fluent APIs
- Light Weight


### Getting Started
- [Kaze - Getting Started](getting-started.md)


### Guide
- User Guide - Creating
- Sample Application - Creating


### Meaning
Kaze means Wind in Japanese.
