package com.lagseeing.fastcdc;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import static com.lagseeing.fastcdc.FastCDC.chunk;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author FengChen
 */
class FastCDCTest {

    static Path resource;
    InputStream inputStream;

    @BeforeAll
    public static void beforeAll() throws URISyntaxException {
        resource = Path.of(Objects.requireNonNull(FastCDCTest.class.getClassLoader()
                .getResource("SekienAkashita.jpg"))
            .toURI());
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        inputStream = Files.newInputStream(resource);
    }

    @AfterEach
    public void afterEach() throws IOException {
        inputStream.close();
    }

    @Test
    public void minimumTooLow() {
        assertThatThrownBy(() -> chunk(
            inputStream, new FastCDCOption().setMinSize(63)
                .setAvgSize(256)
                .setMaxSize(1024))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void minimumTooHigh() {
        assertThatThrownBy(() -> chunk(
            inputStream, new FastCDCOption().setMinSize(67_108_867)
                .setAvgSize(256)
                .setMaxSize(1024))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void averageTooLow() {
        assertThatThrownBy(() -> chunk(
            inputStream, new FastCDCOption().setMinSize(64)
                .setAvgSize(255)
                .setMaxSize(1024))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void averageTooHigh() {
        assertThatThrownBy(() -> chunk(
            inputStream, new FastCDCOption().setMinSize(64)
                .setAvgSize(268_435_457)
                .setMaxSize(1024))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void maximumTooLow() {
        assertThatThrownBy(() -> chunk(
            inputStream, new FastCDCOption().setMinSize(64)
                .setAvgSize(256)
                .setMaxSize(1023))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void maximumTooHigh() {
        assertThatThrownBy(() -> chunk(
            inputStream, new FastCDCOption().setMinSize(64)
                .setAvgSize(256)
                .setMaxSize(1_073_741_825))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void allZeros() {
        final var array = new byte[10240];
        final var chunkIterator = chunk(
            new ByteArrayInputStream(array), new FastCDCOption().setAvgSize(256)
                .setMinSize(64)
                .setMaxSize(1024));
        final var chunks = IteratorUtils.toList(chunkIterator);
        assertThat(chunks).hasSize(10);
        for (final var chunk : chunks) {
            assertThat(chunk.getOffset() % 1024).isZero();
            assertThat(chunk.getLength()).isEqualTo(1024);
        }
    }

    @Test
    public void test_sekien_16k_chunks() {
        final var chunkIterator = chunk(
            inputStream, new FastCDCOption().setAvgSize(16384)
                .setMinSize(8192)
                .setMaxSize(32768));
        final var chunks = IteratorUtils.toList(chunkIterator);
        assertThat(chunks).hasSize(6);
        assertThat(chunks.get(0)
            .getOffset()).isEqualTo(0);
        assertThat(chunks.get(0)
            .getLength()).isEqualTo(22366);
        assertThat(chunks.get(1)
            .getOffset()).isEqualTo(22366);
        assertThat(chunks.get(1)
            .getLength()).isEqualTo(8282);
        assertThat(chunks.get(2)
            .getOffset()).isEqualTo(30648);
        assertThat(chunks.get(2)
            .getLength()).isEqualTo(16303);
        assertThat(chunks.get(3)
            .getOffset()).isEqualTo(46951);
        assertThat(chunks.get(3)
            .getLength()).isEqualTo(18696);
        assertThat(chunks.get(4)
            .getOffset()).isEqualTo(65647);
        assertThat(chunks.get(4)
            .getLength()).isEqualTo(32768);
        assertThat(chunks.get(5)
            .getOffset()).isEqualTo(98415);
        assertThat(chunks.get(5)
            .getLength()).isEqualTo(11051);
    }

    @Test
    public void test_sekien_32k_chunks() {
        final var chunkIterator = chunk(
            inputStream, new FastCDCOption().setAvgSize(32768)
                .setMinSize(16384)
                .setMaxSize(65536));
        final var chunks = IteratorUtils.toList(chunkIterator);
        assertThat(chunks).hasSize(3);
        assertThat(chunks.get(0)
            .getOffset()).isEqualTo(0);
        assertThat(chunks.get(0)
            .getLength()).isEqualTo(32857);
        assertThat(chunks.get(1)
            .getOffset()).isEqualTo(32857);
        assertThat(chunks.get(1)
            .getLength()).isEqualTo(16408);
        assertThat(chunks.get(2)
            .getOffset()).isEqualTo(49265);
        assertThat(chunks.get(2)
            .getLength()).isEqualTo(60201);
    }

    @Test
    public void test_sekien_64k_chunks() {
        final var chunkIterator = chunk(
            inputStream, new FastCDCOption().setAvgSize(65536)
                .setMinSize(32768)
                .setMaxSize(131072));
        final var chunks = IteratorUtils.toList(chunkIterator);
        assertThat(chunks).hasSize(2);
        assertThat(chunks.get(0)
            .getOffset()).isEqualTo(0);
        assertThat(chunks.get(0)
            .getLength()).isEqualTo(32857);
        assertThat(chunks.get(1)
            .getOffset()).isEqualTo(32857);
        assertThat(chunks.get(1)
            .getLength()).isEqualTo(76609);
    }

    @Test
    public void test_chunk_generator_py_fat() throws IOException {
        final var chunkIterator = chunk(
            inputStream, new FastCDCOption().setAvgSize(1024)
                .setMinSize(256)
                .setMaxSize(8192)
                .setFetchData(true)
                .setHashFunction(DigestUtils::md5Hex));
        final var chunks = IteratorUtils.toList(chunkIterator);
        assertThat(chunks).hasSize(97);
        for (final var chunk : chunks) {
            try (final var channel = Files.newByteChannel(resource, StandardOpenOption.READ)) {
                final var buffer = ByteBuffer.allocate(chunk.getLength());
                channel.position(chunk.getOffset())
                    .read(buffer);
                assertThat(buffer.array()).isEqualTo(chunk.getData());
                assertThat(DigestUtils.md5Hex(buffer.array())).isEqualTo(chunk.getHash());
            }
        }
    }
}