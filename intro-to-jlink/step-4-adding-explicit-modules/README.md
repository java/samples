# Creating and Adding a Module to a Runtime
Adding modules to a runtime image isn't restricted to JDK or other pre-existing modules, we can create our own! We will walk through those steps in this exercise.

## Creating a Module
To create a module we will need to provide a `module-info.java`. `module-info.java` is a special type of Java file used for defining a module. In this file we will provide a name, and the modules our module depends on.

```java
module foo {
        requires org.apache.commons.lang3;
}
```

💡 **Note:** `java.base` is implicitly required by allow modules, so does not need to be defined. 

Next we can create a module, using the `jmod` tool with this command:

```
$ jmod create --class-path .  mods/foo.jmod
```

After a few moments the module will be created, we can add it to a runtime image, just like we did in the previous exercise, just make sure to update `--module-path` to include `mods`. 

```
$ jlink --module-path libs:mods --add-modules java.base,org.apache.commons.lang3,foo --output reverse-string-image 
```

We can run the program like we did in the previous exercise:

```
$ ./reverse-string-image/bin/java foo.ReverseString Java
avaJ
```

[In the next exercise](../step-5-creating-a-launcher) we will use JLink to create a launcher to streamline the process of running an application packaged a jlink artifact.  