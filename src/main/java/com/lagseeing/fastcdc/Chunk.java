package com.lagseeing.fastcdc;

import lombok.Value;

import java.util.Objects;

/**
 * @author FengChen
 */
@Value
public class Chunk {

    long offset;
    int length;
    byte[] data;
    String hash;

    public byte[] getData() {
        return Objects.requireNonNull(data);
    }

    public String getHash() {
        return Objects.requireNonNull(hash);
    }

}
