/*
 * Adapted from jMonkeyEngine RingBuffer.java; compared with v3.6.1-stable.
 * Copyright (c) 2009-2023 jMonkeyEngine.
 * Licensed under the BSD 3-Clause license; see
 * licenses/jmonkeyengine-BSD-3-Clause.txt and THIRD_PARTY_NOTICES.md.
 * Local changes: byte storage, renamed members, index/range access and bulk
 * operations in place of the generic queue and iterator, added argument validation.
 */
package io.github.farleychen.fastcdc;

import static io.github.farleychen.fastcdc.ArrayUtils.EMPTY_BYTE_ARRAY;
import static io.github.farleychen.fastcdc.ArrayUtils.subarray;

/**
 * A circular array (array with wrap-around), adapted from jMonkeyEngine.
 * <p>Compared with
 * <a href="https://github.com/jMonkeyEngine/jmonkeyengine/blob/4de10c34f6d078e0cd1e32c16bdf5303ceb61e26/jme3-ios/src/main/java/com/jme3/util/RingBuffer.java">RingBuffer in jMonkeyEngine v3.6.1-stable</a>.
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