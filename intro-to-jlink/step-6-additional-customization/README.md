# Customizing a Runtime with JLink

JLink provides a number of options for customizing the images it creates. Most of them are provide through plugins. To see a full-list of plugins supported by JLink, use the `--list-plugins` option. 

In this exercise we will look at two plugins, `--generate-cds-archive` and `--compress`. 

## Generating a CDS Archive

CDS is a feature provided in the JDK the can improve startup performance by building a pre-processed archive of the class the JVM will load on startup. You can learn more about CDS by watching this video. 

JLink can create a CDS archive with the `--generate-cds-archive` option. 

```
jlink --module-path libs:mods --add-modules java.base,org.apache.commons.lang3,foo --launcher app=foo/foo.ReverseString --generate-cds-archive --output reverse-string-image-cds
```

This will provide a modest improvement to the execution time for the program. 

## Compressing a Runtime

JLink can also apply compression to an image with the `--compress` option. There are ten levels of compression `zip-[0-9]`, the default is `zip-6`. If we were to provide `zip-9` like in this command:

```
jlink --module-path libs:mods --add-modules java.base,org.apache.commons.lang3,foo --launcher app=foo/foo.ReverseString --compress zip-9 --output reverse-string-image-compressed
```

We should see a pretty significant reduction in image size:

```
Size with zip-9: ~173 MB
Size with zip-6: ~831 MB
```

Though this does come with a modest reduction in application performance from the additional compression.


