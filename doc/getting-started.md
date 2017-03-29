# Kaze Getting Started

<!-- TODO : Table of Contents

- Install
- Routing
- Serving Static Contents

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
```java
Http http = Http.server();

http.get("/user", (req, res) -> {
  // handle request "GET /user".
});
http.post("/user", (req, res) -> {
  // handle request "POST /user".
});
http.put("/user", (req, res) -> {
  // handle request "PUT /user".
});
http.delete("/user/:id", (req, res) -> {
  Long id = req.uri(":id", Long.class);
  // handle request "DELETE /user/*".
  // if request uri is '/user/8', id is 8.
});

http.listen();  // http server starts.
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

