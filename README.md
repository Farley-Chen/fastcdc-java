# Introduction

This package implements the "FastCDC" content defined chunking algorithm in Java.

This Java implementation was written with reference to
[iscc/fastcdc-py](https://github.com/iscc/fastcdc-py), which started as a port of
Nathan Fiedler's [fastcdc-rs](https://github.com/nlfiedler/fastcdc-rs), itself
based on [Ronomon's deduplication implementation](https://github.com/ronomon/deduplication).
Source attribution and upstream implementation history are recorded in
[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).

# Usage

```xml

<dependency>
    <groupId>io.github.farley-chen</groupId>
    <artifactId>fastcdc-java</artifactId>
    <version>1.0.3</version>
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

# License and third-party sources

Original project code is licensed under the [MIT License](LICENSE). Adapted
portions retain their upstream MIT, BSD-3-Clause or Apache-2.0 terms; see
[THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) and [licenses/](licenses/).
These notices and license texts are included under `META-INF/` in the Maven
binary and source JARs.
