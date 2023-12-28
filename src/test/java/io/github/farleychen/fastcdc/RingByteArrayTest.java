package io.github.farleychen.fastcdc;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author FengChen
 */
class RingByteArrayTest {

    @Test
    void isEmpty() {
        final var blob = new RingByteArray(10);
        assertThat(blob.isEmpty()).isTrue();

        blob.add((byte)0);
        assertThat(blob.isEmpty()).isFalse();

        blob.add((byte)1);
        blob.add((byte)2);
        assertThat(blob.isEmpty()).isFalse();

        blob.position(3);
        assertThat(blob.isEmpty()).isTrue();

        blob.add((byte)4);
        blob.add((byte)5);
        assertThat(blob.isEmpty()).isFalse();

        blob.position(1);
        assertThat(blob.isEmpty()).isFalse();

        blob.position(1);
        assertThat(blob.isEmpty()).isTrue();
    }

    @Test
    void size() {
        final var blob = new RingByteArray(10);
        assertThat(blob.size()).isEqualTo(0);

        blob.add((byte)0);
        assertThat(blob.size()).isEqualTo(1);

        blob.add((byte)1);
        blob.add((byte)2);
        assertThat(blob.size()).isEqualTo(3);

        blob.position(3);
        assertThat(blob.size()).isEqualTo(0);

        blob.add((byte)4);
        blob.add((byte)5);
        assertThat(blob.size()).isEqualTo(2);

        blob.position(1);
        assertThat(blob.size()).isEqualTo(1);

        blob.position(1);
        assertThat(blob.size()).isEqualTo(0);
    }

    @Test
    void add() {
        final var blob = new RingByteArray(10);
        assertThat(blob.size()).isEqualTo(0);

        blob.add((byte)0);
        assertThat(blob.size()).isEqualTo(1);

        blob.addAll(new byte[] {1, 2});
        assertThat(blob.size()).isEqualTo(3);

        blob.position(3);
        assertThat(blob.size()).isEqualTo(0);

        blob.addAll(new byte[] {4, 5});
        assertThat(blob.size()).isEqualTo(2);
    }

    @Test
    void addError() {
        final var blob = new RingByteArray(10);
        blob.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertThatThrownBy(() -> blob.add((byte)10)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> blob.addAll(new byte[] {10})).isInstanceOf(RuntimeException.class);
        blob.position(4);
        blob.addAll(new byte[] {10, 11, 12, 13});
        assertThatThrownBy(() -> blob.add((byte)14)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> blob.addAll(new byte[] {14})).isInstanceOf(RuntimeException.class);
    }

    @Test
    void position() {
        var blob = new RingByteArray(10);
        blob.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        blob.position(10);

        blob = new RingByteArray(10);
        blob.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        final var _blob1 = blob;
        assertThatThrownBy(() -> _blob1.position(11)).isInstanceOf(RuntimeException.class);

        blob = new RingByteArray(10);
        blob.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        blob.position(4);
        blob.addAll(new byte[] {10, 11, 12, 13});
        blob.position(10);

        blob = new RingByteArray(10);
        blob.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        blob.position(4);
        blob.addAll(new byte[] {10, 11, 12, 13});
        final var _blob2 = blob;
        assertThatThrownBy(() -> _blob2.position(11)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void get() {
        final var blob = new RingByteArray(10);
        blob.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertThat(blob.get(4)).isEqualTo(((byte)4));
        assertThat(blob.get(9)).isEqualTo(((byte)9));
        assertThatThrownBy(() -> blob.get(10)).isInstanceOf(RuntimeException.class);

        blob.position(4);
        blob.addAll(new byte[] {10, 11, 12, 13});
        assertThat(blob.get(4)).isEqualTo((byte)8);
        assertThat(blob.get(9)).isEqualTo((byte)13);
        assertThatThrownBy(() -> blob.get(10)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void getRange() {
        final var blob = new RingByteArray(10);
        blob.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertThat(blob.getRange(4, 4)).isEqualTo(new byte[] {});
        assertThat(blob.getRange(2, 4)).isEqualTo(new byte[] {2, 3});
        assertThat(blob.getRange(9, 10)).isEqualTo(new byte[] {9});
        assertThatThrownBy(() -> blob.getRange(10, 10)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> blob.getRange(9, 11)).isInstanceOf(RuntimeException.class);

        blob.position(4);
        blob.addAll(new byte[] {10, 11, 12, 13});
        assertThat(blob.getRange(4, 4)).isEqualTo(new byte[] {});
        assertThat(blob.getRange(2, 4)).isEqualTo(new byte[] {6, 7});
        assertThat(blob.getRange(9, 10)).isEqualTo(new byte[] {13});
        assertThatThrownBy(() -> blob.getRange(10, 10)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> blob.getRange(9, 11)).isInstanceOf(RuntimeException.class);

    }
}