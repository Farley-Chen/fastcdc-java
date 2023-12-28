package io.github.farleychen.fastcdc;

import static io.github.farleychen.fastcdc.ArrayUtils.EMPTY_BYTE_ARRAY;
import static io.github.farleychen.fastcdc.ArrayUtils.subarray;

/**
 * refer to <a
 * href=https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/jme3-ios/src/main/java/com/jme3/util/RingBuffer.java>jmonkeyengine</a>
 * <p>
 * A circular array (array with wrap-around).
 *
 * @author FengChen
 */
class RingByteArray {

    /**
     * array elements
     */
    private final byte[] elements;
    /**
     * number of elements in array
     */
    private int size = 0;
    /**
     * index of first element of array
     */
    private int firstElementIndex = 0;
    /**
     * index of next available slot
     */
    private int addElementIndex = 0;

    RingByteArray(final int capacity) {
        Assert.isTrue(capacity >= 0);
        elements = new byte[capacity];
    }

    boolean isEmpty() {
        return size == 0;
    }

    int size() {
        return size;
    }

    void addAll(final byte[] bytes) {
        for (final var aByte : bytes) {
            add(aByte);
        }
    }

    void add(final byte aByte) {
        if (size == elements.length) {
            throw new RuntimeException("Ring byte array overflow");
        }
        elements[addElementIndex] = aByte;
        // wrap-around
        addElementIndex = (addElementIndex + 1) % elements.length;
        size++;
    }

    void position(final int index) {
        Assert.isTrue(index >= 0);
        if (index > size) {
            throw new RuntimeException("Ring byte array underflow");
        }
        // wrap-around
        firstElementIndex = (firstElementIndex + index) % elements.length;
        size -= index;
    }

    byte get(final int index) {
        Assert.isTrue(index >= 0);
        if (index >= size) {
            throw new RuntimeException("Ring byte array underflow");
        }
        // wrap-around
        return elements[(firstElementIndex + index) % elements.length];
    }

    byte[] getRange(final int fromInclusive, final int toExclusive) {
        Assert.isTrue(fromInclusive >= 0);
        Assert.isTrue(fromInclusive <= toExclusive);
        if (fromInclusive >= size || toExclusive > size) {
            throw new RuntimeException("Ring byte array underflow");
        }
        if (fromInclusive == toExclusive) {
            return EMPTY_BYTE_ARRAY;
        }
        // wrap-around
        final int from = (firstElementIndex + fromInclusive) % elements.length;
        final int to = (firstElementIndex + toExclusive) % elements.length;
        if (from < to) {
            return subarray(elements, from, to);
        } else {
            return ArrayUtils.addAll(subarray(elements, from, elements.length), subarray(elements, 0, to));
        }
    }

}