package io.github.farleychen.fastcdc;

import java.io.InputStream;

/**
 * @author FengChen
 */
public final class FastCDC {

    public static ChunkIterator chunk(final InputStream stream, final FastCDCOption option) {
        return new ChunkIterator(stream, option.getMinSize(), option.getAvgSize(), option.getMaxSize(),
            option.isFetchData(), option.getHashFunction());
    }

}
