# Spring Web Flux Base Demo

![](https://img.shields.io/badge/build-success-brightgreen.svg)

# Stack

![](https://img.shields.io/badge/java_8-✓-blue.svg)
![](https://img.shields.io/badge/Spring%20WebFlux-✓-blue)
![](https://img.shields.io/badge/lombok-✓-blue)


# File structure

This project is a example for trainning with a lot of tools with java 8, spring WebFlux.

```
SpringDemo2/
 │
 ├── src/main/java/
 │   └── com.example.SpringDemo3
 │       ├── model
 │       │   ├── Comments.java
 │       │   ├── User.java
 │       │   └── UserComments.java
 │       │
 │       └── SpringDemo3Application.java
 │
 ├── src/main/resources/
 │   └── application.properties
 │
 ├── src/main/test/java
 │   └── com.example.SpringDemo3
 │       └── SpringDemo3ApplicationTests.java
 │
 ├── .gitignore
 ├── GITHUB.postman_collection.json
 ├── HELP.md
 ├── README.md
 ├── mvnw
 ├── mvnw.cmd
 └── pom.xml
```

# Introduction (https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)

Basis to begin to understand what is being the reactive programming that will be with Spring WebFlux

# How to use this code?

1. Make sure you have [Java 8](https://www.java.com/download/) and [Maven](https://maven.apache.org) installed

2. Fork this repository and clone it
  
  ```
  $ git clone https://github.com/<your-user>/SpringDemo3
  ```
  
3. Navigate into the folder  

  ```
  $ cd SpringDemo3 (or other name)
  ```

4. Install dependencies

  ```
  $ mvn install
  ```

5. Run the project

  ```
  $ mvn spring-boot:run
  ```