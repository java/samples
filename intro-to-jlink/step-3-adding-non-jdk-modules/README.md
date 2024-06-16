# Adding Non-JDK Modules to a Runtime
In this excerise we will walk through steps of adding a Non-JDK module to a runtime image built with JLink.

## The Program

This is a simple program that's using the `StringUtils.reverse()` located in the [apache commons lang3 library](https://mvnrepository.com/artifact/org.apache.commons/commons-lang3) to reverse a user-provided value. 

💡 **Note:** You will need to download the library from maven central and place it in the `/libs` directory to complete this exercise.

```java
package foo;

import org.apache.commons.lang3.StringUtils;

public class ReverseString{
        public static void main(String... args){
                String reversedString = StringUtils.reverse(args[0]);
                System.out.println(reversedString);
        }
}
```

## Using JDeps to Resolve external JDK Modules 

While we already know that we need to bring in the `org.apache.commons.lang3` module, understanding how to look up this information is important. 

Using `jdeps` we will need to use the `--module-path` option, and pass it the location of where the `apache-commons-lang3.jar` is located, which sould be the `libs` directory.

```
$ jdeps --module-path libs --multi-release 21  foo/ReverseString.class
```

This will return a table, like in the previous excerise.

```
ReverseString.class -> java.base
ReverseString.class -> org.apache.commons.lang3
   foo                                                -> java.io                                            java.base
   foo                                                -> java.lang                                          java.base
   foo                                                -> org.apache.commons.lang3                           org.apache.commons.lang3
```

## Building a Runtime with a Non-JDK Module

Building a runtime with a Non-JDK module is similar to building a runtime like we have done in the previous exercise, we just need to include the path to where the additional modules are located, in this case `libs` like we did in the previous step using `jdeps`:

```
$ jlink --module-path libs --add-modules java.base,org.apache.commons.lang3 --output reverse-string-image
```

We can use this runtime, like in the previous exercises to run the `ReserveString` program:

```
$ ./reverse-string-image/bin/java foo.ReverseString Java
avaJ
```

[In the next exercise](../step-4-adding-explicit-modules) we will walk through the steps of creating our own module and adding it to a runtime image.
