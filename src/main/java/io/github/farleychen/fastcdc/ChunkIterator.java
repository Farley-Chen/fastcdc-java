package io.github.farleychen.fastcdc;

import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Function;

import static io.github.farleychen.fastcdc.Utils.*;

/**
 * @author FengChen
 */
public class ChunkIterator implements Iterator<Chunk> {

    private final InputStream stream;

    private final int centerSize;
    private final int maskS;
    private final int maskL;
    private final int readSize;
    private final RingByteArray blob;
    private long offset;
    private final int minSize;
    private final int avgSize;
    private final int maxSize;
    private final boolean fetchData;
    private final Function<byte[], String> hashFunction;

    ChunkIterator(
        final InputStream stream, final int minSize, final int avgSize, final int maxSize, final boolean fetchData,
        final Function<byte[], String> hashFunction) {
        Assert.isTrue(stream != null);
        Assert.isTrue(minSize > 0);
        Assert.isTrue(avgSize > 0);
        Assert.isTrue(maxSize > 0);
        Assert.isTrue(minSize <= avgSize);
        Assert.isTrue(avgSize <= maxSize);
        Assert.isTrue(minSize >= Const.MINIMUM_MIN);
        Assert.isTrue(minSize <= Const.MINIMUM_MAX);
        Assert.isTrue(avgSize >= Const.AVERAGE_MIN);
        Assert.isTrue(avgSize <= Const.AVERAGE_MAX);
        Assert.isTrue(maxSize >= Const.MAXIMUM_MIN);
        Assert.isTrue(maxSize <= Const.MAXIMUM_MAX);

        this.stream = stream;
        this.minSize = minSize;
        this.avgSize = avgSize;
        this.maxSize = maxSize;
        this.fetchData = fetchData;
        this.hashFunction = hashFunction;
        centerSize = centerSize(avgSize, minSize, maxSize);
        final int bits = logarithm2(avgSize);
        maskS = mask(bits + 1);
        maskL = mask(bits - 1);

        readSize = Math.max(1024 * 64, maxSize);
        blob = new RingByteArray(readSize * 2);
        blob.addAll(readNBytes(stream, readSize));
        offset = 0;
    }

    @Override
    public boolean hasNext() {
        return !blob.isEmpty();
    }

    @Override
    public Chunk next() {
        if (blob.size() <= maxSize) {
            blob.addAll(readNBytes(stream, readSize));
        }
        final int chunkLength = cdcOffset(blob, minSize, avgSize, maxSize, centerSize, maskS, maskL);
        final byte[] data = fetchData
            ? blob.getRange(0, chunkLength)
            : null;
        final String hash = hashFunction != null
            ? (data != null
            ? hashFunction.apply(data)
            : hashFunction.apply(blob.getRange(0, chunkLength)))
            : null;
        final var chunk = new Chunk(offset, chunkLength, data, hash);
        offset += chunkLength;
        blob.position(chunkLength);
        return chunk;
    }

    private int cdcOffset(
        final RingByteArray blob, final int minSize, final int avgSize, final int maxSize, final int centerSize,
        final int maskS, final int maskL) {
        int pattern = 0;
        int index = minSize;
        final int size = blob.size();
        int barrier = Math.min(centerSize, size);
        while (index < barrier) {
            pattern = (pattern >>> 1) + Const.GEAR[Byte.toUnsignedInt(blob.get(index))];
            if ((pattern & maskS) == 0) {
                return index + 1;
            }
            index++;
        }
        barrier = Math.min(maxSize, size);
        while (index < barrier) {
            pattern = (pattern >>> 1) + Const.GEAR[Byte.toUnsignedInt(blob.get(index))];
            if ((pattern & maskL) == 0) {
                return index + 1;
            }
            index++;
        }
        return index;
    }

}
