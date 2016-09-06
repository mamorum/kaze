# Kaze Getting Started

<!-- TODO : Table of Contents

- Install
- Routing
- Serving Static Contents
- Configuration ?
- Start Application

 -->


## Install
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


## Routing
Routing means selecting application method to handle a request. Kaze determines the app method, using http request method and URI.

In Kaze, `@Http` method annotation represents a route.

```java
@Http({METHOD, URI})
```

- __METHOD__ : Http method (GET, POST, etc). Static import `import static kaze.Http.Method.*;` is useful to define.
- __URI__ : Path (`/`, `/user`, etc). Path variable (such as `/:id`, `/user/:id`, etc) is supported.

Following codes are example of routing.

```java
@Http({POST, "/user"})
public void create(Req req, Res res) {
  // handle request.
} 
```

```java
@Http({DELETE, "/user/:id"})
public void delete(Req req, Res res) {
  Long id = req.uri(":id", Long.class);
  // if request URI is '/user/8',
  // the value of id is 8.
} 
```



## Serving Static Contents
If there are no route for a request, Kaze will find content from `public` directory in the classpath. Please put the static contents on `src/main/resources/public`, if you are using Maven or Gradle.


## Configuration
Comming soon ...


## Start Application
Comming soon ...
