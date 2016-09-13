# Kaze Getting Started

<!-- TODO : Table of Contents

- Install
- Routing
- Serving Static Contents
- Start Application

 -->


## Install
Maven

```xml
<dependency>
  <groupId>com.github.mamorum</groupId>
  <artifactId>kaze</artifactId>
  <version>0.1.0</version>
</dependency>
```

Gradle

```gradle
compile 'com.github.mamorum:kaze:0.1.0'
```


## Routing
Routing means that Kaze determines the application method to handle a request based on http method and uri. 

In Kaze, `@Http` method annotation represents a route.

```java
@Http({METHOD, URI})
```

- __METHOD__ : Request method (GET, POST, etc). Static import `import static kaze.Http.Method.*;` is useful to define.
- __URI__ : Request path (`/`, `/user`, etc). Path variable (such as `/:id`, `/user/:id`, etc) is supported.

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
  // id is 8.
} 
```



## Serving Static Contents
If there are no route for a request, Kaze finds the static contents from `public` directory in the classpath. Please put the files (such as image, js, css, etc) on `src/main/resources/public` like bellow, if you are using Maven or Gradle.

```txt
project-root/
  - src/main/resources/
    - public/
      - index.html -> http://localhost:8080/index.html
      - css/
        - style.css -> http://localhost:8080/css/style.css
```

It is possible to serve static contents from file system (ex. `/var/www`, `C:\var\www`, etc). Please see [Configuration - User Guide](user-guide.md) to configure.


## Start Application
Comming soon ...
