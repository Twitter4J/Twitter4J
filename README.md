# X4J
[&#35;Twitter4J](https://twitter.com/search?q=%23twitter4j&src=typed_query&f=live) is a 100% pure Java library for the Twitter API with no extra dependency. 

[![@t4j_news](https://img.shields.io/twitter/url/https/twitter.com/t4j_news.svg?style=social&label=Follow%20%40t4j_news)](https://twitter.com/t4j_news)

## Requirements
Java 8 or later

## Dependency declaration
Add a dependency declaration to pom.xml, or build.gradle as follows:

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.twitter4j/twitter4j-corej/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.twitter4j/twitter4j-core)

### Maven
```xml
<dependencies>
    <dependency>
        <groupId>org.twitter4j</groupId>
        <artifactId>twitter4j-core</artifactId>
        <version>4.1.2</version>
    </dependency>
</dependencies>
```
### Gradle
```text
dependencies {
    implementation 'org.twitter4j:twitter4j-core:4.1.2'
}
```

### Java modularity

```text
requires org.twitter4j;
```

## Getting started

Acquire an instance configured with twitter4j.properties, tweet "Hello Twitter API!".

#### twitter4j.properties

```properties
oauth.consumerKey=[consumer key]
oauth.consumerSecret=[consumer secret]
oauth.accessToken=[access token]
oauth.accessTokenSecret=[access token secret]
```

#### Main.java

```java
import org.twitter4j.*;
public class Main {
  public static void main(String... args){
    Twitter twitter = Twitter.getInstance();
    twitter.v1().tweets().updateStatus("Hello Twitter API!");
  }
}
```

v1() returns [TwitterV1](https://github.com/Twitter4J/Twitter4J/blob/main/twitter4j-core/src/v1/java/twitter4j/v1/TwitterV1.java) interface which provides various Twitter API V1.1 API resources. tweets() returns [TweetsResources](https://github.com/Twitter4J/Twitter4J/blob/main/twitter4j-core/src/v1/java/twitter4j/v1/TweetsResources.java). 


You can also get a builder object from newBuilder() method to configure the instance with code:

#### Main.java

```java
import org.twitter4j.*;
public class Main {
  public static void main(String... args){
    var twitter = Twitter.newBuilder()
      .oAuthConsumer("consumer key", "consumer secret")
      .oAuthAccessToken("access token", "access token secret")
      .build();
    twitter.v1().tweets().updateStatus("Hello Twitter API!");
  }
}
```

## License
Apache License Version 2.0

![Java CI with Gradle](https://github.com/Twitter4J/Twitter4J/workflows/Java%20CI%20with%20Gradle/badge.svg)
