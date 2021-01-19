# An alternative of logback DateConverter, with 20x performance of the official.

## usage

1. add dependency in pom.xml

```xml
<dependency>
    <groupId>com.jvmbytes</groupId>
    <artifactId>logback-time-converter</artifactId>
    <version>1.0.0</version>
</dependency>
```

2. initial to using `DateTimeConverter` before initial logback:

```java
DateTimeConverter.initialDefaultDateTimeConverter();
```
