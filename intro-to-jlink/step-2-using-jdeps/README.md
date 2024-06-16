# Using JDeps

In this exercise, we will using the `jdeps` tool to resolve the modules we need to include in a runtime.

## The Program

This program is designed to call a url endpoint and print to the JDK logger the response it receives. 

```java
package foo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetWebsite {

  private static final Logger LOGGER = Logger.getLogger(GetWebsite.class.getName());
  public static void main(String... args) throws InterruptedException {
    String url = args[0];
    try (HttpClient client = HttpClient.newHttpClient();) {
      LOGGER.log(Level.INFO, "Calling: " + url);
      HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
      client.sendAsync(request, BodyHandlers.ofString())
        .thenApply(HttpResponse::body).thenAccept(r -> LOGGER.log(Level.INFO, r));
      client.awaitTermination(Duration.ofMillis(1000L));
    }
  }
}
```

## Resolving Module Dependencies with JDeps	
Attempting to run `GetWebsite` with the runtime we created in the previous exercise will fail, throwing `ClassNotFound` exceptions. This is because some of the classes referenced in `GetWebsite` are located outside the `java.base` module. 

The `jdeps` tool can be used to find which modules `GetWebsite` depends upon, which can be done with:

```
$jdeps GetWebsite.class
```

Which will return this table:

```
jdeps GetWebsite.class
GetWebsite.class -> java.base
GetWebsite.class -> java.logging
GetWebsite.class -> java.net.http
   foo                                                -> java.lang                                          java.base
   foo                                                -> java.lang.invoke                                   java.base
   foo                                                -> java.net                                           java.base
   foo                                                -> java.net.http                                      java.net.http
   foo                                                -> java.time                                          java.base
   foo                                                -> java.util.concurrent                               java.base
   foo                                                -> java.util.function                                 java.base
   foo                                                -> java.util.logging                                  java.logging
```

The top of the table describes the modules the class depends on, the bottom portion shows how those values were calculated. 

JDeps can also be configured to provide a more user friendly output with the option `--print-module-deps` which will return a comma-separated list of the modules:

```
$ jdeps --print-module-deps GetWebsite.class
java.base,java.logging,java.net.http
```

This can then be copied into jlink to build runtime that can run `GetWebsite` like in this command:

```
$ jlink --add-modules java.base,java.logging,java.net.http --output get-website-image
```

This runtime can be used to successfully execute `GetWebsite`

```
$ ./get-website-image/bin/java GetWebsite http://localhost:8000
```

[In the next exercise](../step-3-adding-non-jdk-modules) we will look at how to add non-JDK modules to a runtime image created with JLink. 