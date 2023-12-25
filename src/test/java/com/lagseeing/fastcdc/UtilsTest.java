package com.lagseeing.fastcdc;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author FengChen
 */
class UtilsTest {

    @Test
    void logarithm2() {
        assertThat(Utils.logarithm2(65537)).isEqualTo(16);
        assertThat(Utils.logarithm2(65536)).isEqualTo(16);
        assertThat(Utils.logarithm2(65535)).isEqualTo(16);
        assertThat(Utils.logarithm2(32769)).isEqualTo(15);
        assertThat(Utils.logarithm2(32768)).isEqualTo(15);
        assertThat(Utils.logarithm2(32767)).isEqualTo(15);
    }

    @Test
    void mask() {
        assertThat(Utils.mask(24)).isEqualTo(16_777_215);
        assertThat(Utils.mask(16)).isEqualTo(65535);
        assertThat(Utils.mask(10)).isEqualTo(1023);
        assertThat(Utils.mask(8)).isEqualTo(255);
        assertThatThrownBy(() -> Utils.mask(0)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void centerSize() {
        assertThat(Utils.centerSize(50, 100, 50)).isEqualTo(0);
        assertThat(Utils.centerSize(200, 100, 50)).isEqualTo(50);
        assertThat(Utils.centerSize(200, 100, 40)).isEqualTo(40);
    }

    @Test
    void ceilDiv() {
        assertThat(Utils.ceilDiv(10, 5)).isEqualTo(2);
        assertThat(Utils.ceilDiv(11, 5)).isEqualTo(3);
        assertThat(Utils.ceilDiv(10, 3)).isEqualTo(4);
        assertThat(Utils.ceilDiv(9, 3)).isEqualTo(3);
        assertThat(Utils.ceilDiv(6, 2)).isEqualTo(3);
        assertThat(Utils.ceilDiv(5, 2)).isEqualTo(3);
    }
}