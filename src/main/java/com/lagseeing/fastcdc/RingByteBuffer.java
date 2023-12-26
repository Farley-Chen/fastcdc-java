package com.lagseeing.fastcdc;

import org.apache.commons.lang3.ArrayUtils;

import static org.apache.commons.lang3.ArrayUtils.EMPTY_BYTE_ARRAY;
import static org.apache.commons.lang3.ArrayUtils.subarray;

/**
 * refer to <a
 * href=https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/jme3-ios/src/main/java/com/jme3/util/RingBuffer.java>jmonkeyengine</a>
 * <p>
 * Ring buffer (fixed size queue) implementation using a circular array (array with wrap-around).
 *
 * @author FengChen
 */
class RingByteBuffer {

    /**
     * queue elements
     */
    private final byte[] buffer;
    /**
     * number of elements on queue
     */
    private int count = 0;
    /**
     * index of first element of queue
     */
    private int indexOut = 0;
    /**
     * index of next available slot
     */
    private int indexIn = 0;

    RingByteBuffer(final int capacity) {
        Assert.isTrue(capacity >= 0);
        buffer = new byte[capacity];
    }

    boolean isEmpty() {
        return count == 0;
    }

    int size() {
        return count;
    }

    void addAll(final byte[] bytes) {
        for (final var aByte : bytes) {
            add(aByte);
        }
    }

    void add(final byte aByte) {
        if (count == buffer.length) {
            throw new RuntimeException("Ring byte buffer overflow");
        }
        buffer[indexIn] = aByte;
        // wrap-around
        indexIn = (indexIn + 1) % buffer.length;
        count++;
    }

    void position(final int index) {
        Assert.isTrue(index >= 0);
        if (count < index) {
            throw new RuntimeException("Ring byte buffer underflow");
        }
        // wrap-around
        indexOut = (indexOut + index) % buffer.length;
        count -= index;
    }

    byte get(final int index) {
        Assert.isTrue(index >= 0);
        if (count <= index) {
            throw new RuntimeException("Ring byte buffer underflow");
        }
        // wrap-around
        return buffer[(indexOut + index) % buffer.length];
    }

    byte[] getRange(final int fromInclusive, final int toExclusive) {
        Assert.isTrue(fromInclusive >= 0);
        Assert.isTrue(fromInclusive <= toExclusive);
        if (count <= fromInclusive || count < toExclusive) {
            throw new RuntimeException("Ring byte buffer underflow");
        }
        if (fromInclusive == toExclusive) {
            return EMPTY_BYTE_ARRAY;
        }
        // wrap-around
        final int from = (indexOut + fromInclusive) % buffer.length;
        final int to = (indexOut + toExclusive) % buffer.length;
        if (from < to) {
            return subarray(buffer, from, to);
        } else {
            return ArrayUtils.addAll(subarray(buffer, from, buffer.length), subarray(buffer, 0, to));
        }
    }

}