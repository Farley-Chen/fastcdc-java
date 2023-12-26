package io.github.farleychen.fastcdc;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * @author FengChen
 */
final class Utils {

    static byte[] readNBytes(final InputStream stream, final int readSize) {
        final byte[] bytes;
        try {
            bytes = stream.readNBytes(readSize);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return bytes;
    }

    static int logarithm2(final int value) {
        return (int)Math.round(Math.log(value) / Math.log(2));
    }

    static int mask(final int bits) {
        Assert.isTrue(bits >= 1);
        Assert.isTrue(bits <= 31);
        return (int)(Math.pow(2, bits) - 1);
    }

    static int centerSize(final int average, final int minimum, final int sourceSize) {
        int offset = minimum + ceilDiv(minimum, 2);
        if (offset > average) {
            offset = average;
        }
        final int size = average - offset;
        return Math.min(size, sourceSize);
    }

    static int ceilDiv(final int x, final int y) {
        return (x + y - 1) / y;
    }

}
