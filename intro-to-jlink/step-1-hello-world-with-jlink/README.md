# Hello World with JLink

In this exercise we will create a simple custom runtime image with JLink that cna be used to run a Hello World program.

## The Program

The program is a standard "Hello World!", it is located in the `foo` package. 

```java
package foo;

public class HelloWorld{
        public static void main(String[] args){
                System.out.println("Hello JLink!");
        }
}
```

## Creating a Runtime Image with JLink

To create a runtime image with jlink that can run `HelloWorld` we will run the following command:

```
$ jlink --add-modules java.base --output hello-jlink-image
```

In the above command:

*  `--add-modules`: defines the modules to be added to the image. `java.base` all the classes needed to execute `HelloWorld`

* `--output`: is the name of the resulting image produced by `jlink`. This can be arbitrary, but should be descriptive. 

## Running an Application with a JLink Image

Running an application with a jlink image would be no different than with the JDK, just reference the `java` command located in the bin directory.

```
$ ./hello-jlink-image/bin/java foo.HelloWorld
```

Which should return:

```
Hello JLink!
```

[In the next exercise](../step-2-using-jdeps) we will look at using JDeps to find out which modules to include in an image.