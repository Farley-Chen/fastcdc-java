# Introduction

This package implements the "FastCDC" content defined chunking algorithm in Java.

This package started as Java port of the implementation by Nathan Fiedler, and the code refer to `iscc/fastcdc-py`

- [nlfiedler/fastcdc-rs](https://github.com/nlfiedler/fastcdc-rs)
- [iscc/fastcdc-py](https://github.com/iscc/fastcdc-py)

# Usage

```xml

<dependency>
    <groupId>io.github.Farley-Chen</groupId>
    <artifactId>fastcdc-java</artifactId>
    <version>1.0.1</version>
</dependency>
```

```java
package io.github.farleychen.fastcdc;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Example {

    public static void main(final String[] args) throws IOException {
        try (final var inputStream = Files.newInputStream(Path.of("xxx"))) {
            final var chunkIterator = FastCDC.chunk(
                inputStream, new FastCDCOption().setAvgSize(16384)
                    .setMinSize(8192)
                    .setMaxSize(32768)
                    .setFetchData(true)
                    .setHashFunction(DigestUtils::md5Hex));
            chunkIterator.forEachRemaining(chunk -> {
                System.out.println(chunk.getOffset());
                System.out.println(chunk.getLength());
                System.out.println(chunk.getHash());
            });
        }
    }

}
```
