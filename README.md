# fastcdc-java

A Java implementation of the [FastCDC](https://www.usenix.org/conference/atc16/technical-sessions/presentation/xia) (Fast Content-Defined Chunking) algorithm.

FastCDC splits data into variable-sized chunks based on their content, making it useful for data deduplication, incremental backups, and content-addressable storage.

This library provides stream-based chunking through Java's `InputStream` API, with configurable chunk sizes and optional content hashing.

## Features

- Content-defined chunking using FastCDC
- Sequential processing of Java `InputStream` sources
- Configurable minimum, average, and maximum chunk sizes
- Optional chunk data retrieval and custom hashing
- Standard Java `Iterator<Chunk>` API
- Java 11+

## Installation

Available on Maven Central.

**Maven**

```xml
<dependency>
    <groupId>io.github.farley-chen</groupId>
    <artifactId>fastcdc-java</artifactId>
    <version>1.0.3</version>
</dependency>
```

**Gradle (Kotlin DSL)**

```kotlin
implementation("io.github.farley-chen:fastcdc-java:1.0.3")
```

## Usage

### Basic Example

```java
import io.github.farleychen.fastcdc.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Example {

    public static void main(String[] args) throws IOException {
        FastCDCOption options = new FastCDCOption()
                .setAvgSize(16384)
                .setMinSize(8192)
                .setMaxSize(32768)
                .setFetchData(true);

        try (InputStream input = Files.newInputStream(Path.of("example.bin"))) {
            ChunkIterator chunks = FastCDC.chunk(input, options);

            while (chunks.hasNext()) {
                Chunk chunk = chunks.next();

                System.out.printf(
                        "Offset: %d, Length: %d%n",
                        chunk.getOffset(),
                        chunk.getLength()
                );

                byte[] data = chunk.getData();
                // Process chunk data
            }
        }
    }
}
```

Each chunk contains its offset and length within the original input. Raw chunk data is available when `fetchData` is enabled.

An empty input produces no chunks. The final chunk may be smaller than the configured minimum size.

The input stream is managed by the caller and should be closed after processing.

### Configuration

Chunking behavior can be customized using `FastCDCOption`.

| Option | Default | Description |
|---|---|---|
| `minSize` | `avgSize / 4` | Minimum chunk size in bytes |
| `avgSize` | `8192` | Target average chunk size in bytes |
| `maxSize` | `avgSize * 8` | Maximum chunk size in bytes |
| `fetchData` | `false` | Include raw chunk data |
| `hashFunction` | `null` | Optional function for hashing chunk data |

Chunk sizes must satisfy `minSize <= avgSize <= maxSize`, along with the supported size limits enforced by the library.

The average size is a target, not a guaranteed chunk length.

### Custom Hashing

A custom hash function can be provided using `setHashFunction()`.

For example, using SHA-256 from the Java standard library:

```java
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// Inside a method declaring throws NoSuchAlgorithmException:
MessageDigest digest = MessageDigest.getInstance("SHA-256");

FastCDCOption options = new FastCDCOption()
        .setHashFunction(data ->
                Base64.getEncoder().encodeToString(digest.digest(data)));
```

The hash function receives the raw bytes of each chunk and returns a string representation of its hash.

The resulting value is available through `chunk.getHash()`.

Hashing and data retrieval are independent options. A hash can be calculated even when `fetchData` is disabled.

Call `getData()` or `getHash()` only when their corresponding values are available. Otherwise, these methods throw `NullPointerException`.

## Implementation

The core chunk-boundary detection logic is adapted from [iscc/fastcdc-py](https://github.com/iscc/fastcdc-py).

This Java implementation provides an `InputStream`-based API with a standard `Iterator<Chunk>` interface. It uses a reusable ring buffer, adapted from jMonkeyEngine's `RingBuffer`, to retain unconsumed bytes across chunk boundaries without moving them when refilling the buffer.

These adaptations focus on Java API integration and buffering while preserving the core FastCDC chunk-boundary detection algorithm.

## Downstream Projects

### [GrapheneOS — Seedvault Chunker](https://github.com/GrapheneOS/seedvault-chunker)

[GrapheneOS](https://grapheneos.org/) is a privacy- and security-focused Android operating system. Its Seedvault Chunker is a Kotlin implementation of FastCDC for Android backups that explicitly credits `fastcdc-java` as an upstream implementation.

## References

- [FastCDC: A Fast and Efficient Content-Defined Chunking Approach for Data Deduplication](https://www.usenix.org/conference/atc16/technical-sessions/presentation/xia) — Original FastCDC paper
- [iscc/fastcdc-py](https://github.com/iscc/fastcdc-py) — Reference implementation for the core chunking logic
- [nlfiedler/fastcdc-rs](https://github.com/nlfiedler/fastcdc-rs) — Earlier Rust implementation
- [ronomon/deduplication](https://github.com/ronomon/deduplication) — Earlier FastCDC implementation

## License

Original project code is licensed under the [MIT License](LICENSE).

Portions adapted from third-party projects retain their respective license terms. See [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) and [licenses/](licenses/) for source attribution, copyright notices, and license texts.

The license and attribution files are also included in the Maven binary and source JARs.