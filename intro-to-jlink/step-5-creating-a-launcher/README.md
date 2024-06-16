# Creating a Launcher with JLink
For Java developers distributing an artifact to clients, streamlining the process of starting a running the application has many benefits. JLink provides the ability to include a launcher which can provide a mapping to the main class with a JLink image. 

## Adding a Launcher to a JLink Image

The process of adding a launcher is simple. Just include the `--launcher` option and provide a name, and the module and fully qualified class-path to the main class like in this command:

```
$ jlink --add-modules java.base,foo,org.apache.commons.lang3 --module-path mods:libs --launcher app=foo/foo.ReverseString --output reverse-string-image
```

With the launcher added, we can simply reference the launcher `app` instead of main class:

```
$ ./reverse-string-image/bin/app Java
avaJ
```

[In the next exercise](../step-6-additional-customization) we will look at a couple of options JLink provides for customizing a runtime.