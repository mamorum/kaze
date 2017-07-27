# Draft of Kaze User Guide

<!-- TODO : Table of Contents

- Configuration
- Logging

 -->


## Configuration
Comming soon ...


## Logging
### Install logback-classic
Kaze depends on [SLF4J](http://www.slf4j.org/index.html). To output logging, please add logback-classic to your app.

```xml
<dependency>
  <groupId>ch.qos.logback</groupId>
  <artifactId>logback-classic</artifactId>
</dependency>
```

Latest version of Logback-aclassic can be checked in [Central Repository](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22ch.qos.logback%22%20AND%20a%3A%22logback-classic%22) or [Logback Web Site](http://logback.qos.ch/).


### Configuration of Logback
[Logback documentation](http://logback.qos.ch/documentation.html) is useful to configure logging.

### Other logging implementation
The way to switch logging frameworks is written in the [SLF4J user manual](http://www.slf4j.org/manual.html).


## Http access logging
To output access log, please create `logback-access.xml` in the classpath root. [Logback-access configuration](http://logback.qos.ch/access.html#configuration) is useful to write xml.
