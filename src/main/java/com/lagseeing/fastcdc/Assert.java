package com.lagseeing.fastcdc;

/**
 * @author FengChen
 */
final class Assert {

    static void isTrue(final boolean condition) {
        if (condition) {
            return;
        }
        throw new IllegalArgumentException();
    }

}
