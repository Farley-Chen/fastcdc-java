package io.github.farleychen.fastcdc;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Function;

/**
 * @author FengChen
 */
@Data
@Accessors(chain = true)
public class FastCDCOption {

    private int minSize = -1;
    private int avgSize = 8192;
    private int maxSize = -1;
    private boolean fetchData = false;
    private Function<byte[], String> hashFunction = null;

    public int getMinSize() {
        return minSize == -1
            ? avgSize / 4
            : minSize;
    }

    public int getMaxSize() {
        return maxSize == -1
            ? avgSize * 8
            : maxSize;
    }

}
