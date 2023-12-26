package io.github.farleychen.fastcdc;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author FengChen
 */
class RingByteBufferTest {

    @Test
    void isEmpty() {
        final var buffer = new RingByteBuffer(10);
        assertThat(buffer.isEmpty()).isTrue();

        buffer.add((byte)0);
        assertThat(buffer.isEmpty()).isFalse();

        buffer.add((byte)1);
        buffer.add((byte)2);
        assertThat(buffer.isEmpty()).isFalse();

        buffer.position(3);
        assertThat(buffer.isEmpty()).isTrue();

        buffer.add((byte)4);
        buffer.add((byte)5);
        assertThat(buffer.isEmpty()).isFalse();

        buffer.position(1);
        assertThat(buffer.isEmpty()).isFalse();

        buffer.position(1);
        assertThat(buffer.isEmpty()).isTrue();
    }

    @Test
    void size() {
        final var buffer = new RingByteBuffer(10);
        assertThat(buffer.size()).isEqualTo(0);

        buffer.add((byte)0);
        assertThat(buffer.size()).isEqualTo(1);

        buffer.add((byte)1);
        buffer.add((byte)2);
        assertThat(buffer.size()).isEqualTo(3);

        buffer.position(3);
        assertThat(buffer.size()).isEqualTo(0);

        buffer.add((byte)4);
        buffer.add((byte)5);
        assertThat(buffer.size()).isEqualTo(2);

        buffer.position(1);
        assertThat(buffer.size()).isEqualTo(1);

        buffer.position(1);
        assertThat(buffer.size()).isEqualTo(0);
    }

    @Test
    void add() {
        final var buffer = new RingByteBuffer(10);
        assertThat(buffer.size()).isEqualTo(0);

        buffer.add((byte)0);
        assertThat(buffer.size()).isEqualTo(1);

        buffer.addAll(new byte[] {1, 2});
        assertThat(buffer.size()).isEqualTo(3);

        buffer.position(3);
        assertThat(buffer.size()).isEqualTo(0);

        buffer.addAll(new byte[] {4, 5});
        assertThat(buffer.size()).isEqualTo(2);
    }

    @Test
    void addError() {
        final var buffer = new RingByteBuffer(10);
        buffer.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertThatThrownBy(() -> buffer.add((byte)10)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> buffer.addAll(new byte[] {10})).isInstanceOf(RuntimeException.class);
        buffer.position(4);
        buffer.addAll(new byte[] {10, 11, 12, 13});
        assertThatThrownBy(() -> buffer.add((byte)14)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> buffer.addAll(new byte[] {14})).isInstanceOf(RuntimeException.class);
    }

    @Test
    void position() {
        var buffer = new RingByteBuffer(10);
        buffer.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        buffer.position(10);

        buffer = new RingByteBuffer(10);
        buffer.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        final var _buffer1 = buffer;
        assertThatThrownBy(() -> _buffer1.position(11)).isInstanceOf(RuntimeException.class);

        buffer = new RingByteBuffer(10);
        buffer.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        buffer.position(4);
        buffer.addAll(new byte[] {10, 11, 12, 13});
        buffer.position(10);

        buffer = new RingByteBuffer(10);
        buffer.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        buffer.position(4);
        buffer.addAll(new byte[] {10, 11, 12, 13});
        final var _buffer2 = buffer;
        assertThatThrownBy(() -> _buffer2.position(11)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void get() {
        final var buffer = new RingByteBuffer(10);
        buffer.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertThat(buffer.get(4)).isEqualTo(((byte)4));
        assertThat(buffer.get(9)).isEqualTo(((byte)9));
        assertThatThrownBy(() -> buffer.get(10)).isInstanceOf(RuntimeException.class);

        buffer.position(4);
        buffer.addAll(new byte[] {10, 11, 12, 13});
        assertThat(buffer.get(4)).isEqualTo((byte)8);
        assertThat(buffer.get(9)).isEqualTo((byte)13);
        assertThatThrownBy(() -> buffer.get(10)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void getRange() {
        final var buffer = new RingByteBuffer(10);
        buffer.addAll(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        assertThat(buffer.getRange(4, 4)).isEqualTo(new byte[] {});
        assertThat(buffer.getRange(2, 4)).isEqualTo(new byte[] {2, 3});
        assertThat(buffer.getRange(9, 10)).isEqualTo(new byte[] {9});
        assertThatThrownBy(() -> buffer.getRange(10, 10)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> buffer.getRange(9, 11)).isInstanceOf(RuntimeException.class);

        buffer.position(4);
        buffer.addAll(new byte[] {10, 11, 12, 13});
        assertThat(buffer.getRange(4, 4)).isEqualTo(new byte[] {});
        assertThat(buffer.getRange(2, 4)).isEqualTo(new byte[] {6, 7});
        assertThat(buffer.getRange(9, 10)).isEqualTo(new byte[] {13});
        assertThatThrownBy(() -> buffer.getRange(10, 10)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> buffer.getRange(9, 11)).isInstanceOf(RuntimeException.class);

    }
}