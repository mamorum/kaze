# Kaze Getting Started

<!-- TODO : Table of Contents

- Installing
- Routing
- Serving Static Contents
- Start Application

 -->


## Installing
### kaze
Maven

```xml
<dependency>
  <groupId>com.github.mamorum</groupId>
  <artifactId>kaze</artifactId>
  <version>0.0.2</version>
</dependency>
```

Gradle

```gradle
compile 'com.github.mamorum:kaze:0.0.2'
```

### logback-classic
Maven

```xml
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
</dependency>
```

Latest version can be checked on [Central Repository (logback-classic)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22ch.qos.logback%22%20AND%20a%3A%22logback-classic%22) or [Logback Web Site](http://logback.qos.ch/).

Kaze depends on [SLF4J](http://www.slf4j.org/index.html). To output logging, please add logback-classic or other logging implementation to your app. The way to switch logging frameworks is written in the [SLF4J user manual](http://www.slf4j.org/manual.html).


## Routing
Routing means selecting application method to handle a request. Kaze determines the app method, using http request method and URI.

In Kaze, `@Http` method annotation represents a route.

```java
@Http({METHOD, URI})
```

- __METHOD__ is a http method (GET, POST, etc), defined also in [kaze.Http](https://github.com/mamorum/kaze/blob/master/src/main/java/kaze/Http.java)
- __URI__ is a path (`/`, `/user`, etc)
- __URI variable__ is supported (`/:id`, `/user/:name`, etc)

Following codes are example of routing.

```java
@Http({POST, "/user"})
public void create(Req req, Res res) {
  // create user.
} 
```

```java
@Http({DELETE, "/user/:id"})
public void delete(Req req, Res res) {
  Long id = req.uri(":id", Long.class);
  // delete user, using id.
} 
```

Static import `import static kaze.Http.Method.*` is convenient to define http method (GET, POST, etc) at `@Http`.

## Serving Static Contents
Comming Soon ...

